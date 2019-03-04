package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.annotation;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration.IlluminatiGSFilterConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class IlluminatiGracefulShutdownImpl implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        new IlluminatiGSFilterConfiguration(applicationContext);
    }
}
