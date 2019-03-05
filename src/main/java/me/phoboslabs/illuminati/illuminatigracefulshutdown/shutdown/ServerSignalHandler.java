package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration.IlluminatiGSFilterConfiguration;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.RequiredValueException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.SignalNotSupportException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler.ShutdownHandler;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ServerSignalHandler implements SignalHandler {

    private final ShutdownHandler shutdownHandler;
    private SignalHandler signalHandler;

    private int retryCount = 3;

    private static boolean INITIALIZED = false;

    private ServerSignalHandler(final String signalName, ShutdownHandler shutdownHandler) throws SignalNotSupportException {
        if (signalName.equalsIgnoreCase("TERM") == false) {
            throw new SignalNotSupportException();
        }

        Signal signal = new Signal(signalName);

        this.shutdownHandler = shutdownHandler;
        this.signalHandler = Signal.handle(signal, this);

        if (this.shutdownHandler != null && this.signalHandler != null) {
            INITIALIZED = true;
        }
    }

    @Override
    public void handle(Signal signal) {
        IlluminatiGSFilterConfiguration.setReadyToShutdown(signal.getName());

        try {
            if (this.retryCount > 0) {
                final long timeLimit = 30000L;
                long checkTimeData = 0L;

                while ((IlluminatiGSFilterConfiguration.getWorkCount() > 0) && (checkTimeData <= timeLimit)) {
                    try {
                        Thread.sleep(100L);
                        checkTimeData += 100L;
                    } catch (InterruptedException ignored) {}
                }

                try {
                    this.shutdownHandler.stopApplication();
                } catch (Exception ex) {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("@ Your Application is finished by force. BYE BYE.   @");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                    System.exit(0);
                }

                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException ignored) {}
            }
        } finally {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("@ Your Application is finished. BYE BYE.            @");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            if (((this.signalHandler != SIG_DFL && this.signalHandler != SIG_IGN)
                    || (this.shutdownHandler.isFinished() == false))
                    && this.retryCount > 0) {
                this.retryCount--;
                this.signalHandler.handle(signal);
            } else {
                System.exit(0);
            }
        }
    }

    public static ServerSignalHandler registShutdownHandler(String signalName, ShutdownHandler serverShutdownHandler) throws SignalNotSupportException, RequiredValueException {
        if (serverShutdownHandler == null) {
            throw new RequiredValueException();
        }
        return new ServerSignalHandler(signalName, serverShutdownHandler);
    }

    public static boolean isInitialized () {
        return INITIALIZED;
    }
}
