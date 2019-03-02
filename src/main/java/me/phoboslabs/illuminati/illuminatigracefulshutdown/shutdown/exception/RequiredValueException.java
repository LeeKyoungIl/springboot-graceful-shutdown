package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception;

public class RequiredValueException extends Exception {

    private static final String EXCEPTION_MESSAGE = "The serverShutdownHandler can't be null.";

    public RequiredValueException () {
        super(EXCEPTION_MESSAGE);
    }
}
