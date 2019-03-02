package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception;

public class SignalNotSupportException extends Exception {

    private static final String SIGNAL_NOT_SUPPORT_MESSAGE = "Check your the signal command message.";

    public SignalNotSupportException () {
        super(SIGNAL_NOT_SUPPORT_MESSAGE);
    }
}
