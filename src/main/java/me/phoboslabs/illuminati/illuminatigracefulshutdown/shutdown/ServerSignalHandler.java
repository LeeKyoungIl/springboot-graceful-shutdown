package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.RequiredValueException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.SignalNotSupportException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler.ShutdownHandler;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ServerSignalHandler implements SignalHandler {

    private final ShutdownHandler shutdownHandler;
    private SignalHandler signalHandler;

    private static int retryCount = 3;
    private final long timeLimit = 30000L;

    private ServerSignalHandler(final String signalName, ShutdownHandler shutdownHandler) throws SignalNotSupportException {
        if (signalName.equalsIgnoreCase("TERM") == false) {
            throw new SignalNotSupportException();
        }

        Signal signal = new Signal(signalName);

        this.shutdownHandler = shutdownHandler;
        this.signalHandler = Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal signal) {
        try {
            this.shutdownHandler.stopApplication();
            if (this.retryCount > 0) {
                long checkTimeData = 0L;

                while (ServerSignalFilter.getWorkCount() > 0 && checkTimeData <= this.timeLimit) {
                    try {
                        Thread.sleep(100L);
                        checkTimeData += 100L;
                    } catch (InterruptedException ignored) {
                    }
                }
            } else {
                System.exit(0);
            }
        } finally {
            if (this.signalHandler != SIG_DFL && this.signalHandler != SIG_IGN) {
                this.retryCount--;
                this.signalHandler.handle(signal);
            }

            if (this.shutdownHandler.isFinishied()) {
                System.exit(0);
            } else {
                this.signalHandler.handle(signal);
            }
        }
    }

    public static ServerSignalHandler registShutdownHandler(String signalName, ShutdownHandler serverShutdownHandler) throws SignalNotSupportException, RequiredValueException {
        if (serverShutdownHandler == null) {
            throw new RequiredValueException();
        }
        return new ServerSignalHandler(signalName, serverShutdownHandler);
    }
}
