package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler.ShutdownHandler;
import org.springframework.util.Assert;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ServerSignalHandler implements SignalHandler {

    private final ShutdownHandler shutdownHandler;
    private SignalHandler signalHandler;

    private ServerSignalHandler(final String signalName, ShutdownHandler shutdownHandler) {
        Signal signal = new Signal(signalName);

        this.shutdownHandler = shutdownHandler;
        this.signalHandler = Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal signal) {
        System.out.println("tt");

        this.shutdownHandler.stopApplication();

        while (ServerSignalFilter.getWorkCount() > 0) {
            try {
                System.out.println(ServerSignalFilter.getWorkCount());
                Thread.sleep(100L);
            } catch (InterruptedException ignored) {
            }
        }

        if (this.signalHandler != SIG_DFL && this.signalHandler != SIG_IGN) {
            this.signalHandler.handle(signal);
            System.exit(-1);
        } else {
            System.out.println("tt12");
        }
    }

    public static ServerSignalHandler registShutdownHandler(String signalName, ShutdownHandler serverShutdownHandler) {
        Assert.notNull(serverShutdownHandler, "serverShutdownHandler can't be null.");
        return new ServerSignalHandler(signalName, serverShutdownHandler);
    }
}
