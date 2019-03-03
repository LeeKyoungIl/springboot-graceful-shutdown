package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler;

public interface ShutdownHandler {

    void stopApplication ();

    boolean isFinished();
}
