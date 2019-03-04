package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception;

import org.springframework.beans.BeansException;

public class ApplicationContextBeanException extends BeansException {

    private static final String MSG = "The ApplicationContext can't be null.";

    public ApplicationContextBeanException() {
        super(MSG);
    }
}
