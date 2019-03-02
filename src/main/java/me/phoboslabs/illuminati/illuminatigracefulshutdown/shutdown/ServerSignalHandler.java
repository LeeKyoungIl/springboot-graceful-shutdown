package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.RequiredValueException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.SignalNotSupportException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler.ShutdownHandler;
import org.springframework.util.Assert;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ServerSignalHandler implements SignalHandler {

    private final ShutdownHandler shutdownHandler;
    private SignalHandler signalHandler;

    private ServerSignalHandler(final String signalName, ShutdownHandler shutdownHandler) throws SignalNotSupportException {
        if (signalName.equalsIgnoreCase("TERM") == false) {
            throw new SignalNotSupportException();
        }

        Signal signal = new Signal(signalName);
        ServerSignalFilter.setReadyToShutdown(signal);

        this.shutdownHandler = shutdownHandler;
        this.signalHandler = Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal signal) {
        try {
            this.shutdownHandler.stopApplication();

            while (ServerSignalFilter.getWorkCount() > 0) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException ignored) {
                }
            }
        } finally {
            if (this.signalHandler != SIG_DFL && this.signalHandler != SIG_IGN) {
                this.signalHandler.handle(signal);
            }

            System.exit(0);
        }
    }

    public static ServerSignalHandler registShutdownHandler(String signalName, ShutdownHandler serverShutdownHandler) throws SignalNotSupportException, RequiredValueException {
        if (serverShutdownHandler == null) {
            throw new RequiredValueException();
        }
        return new ServerSignalHandler(signalName, serverShutdownHandler);
    }
}
