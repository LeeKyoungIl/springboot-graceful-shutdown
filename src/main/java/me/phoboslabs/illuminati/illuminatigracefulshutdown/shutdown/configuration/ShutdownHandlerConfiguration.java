package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.ServerSignalHandler;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.RequiredValueException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.SignalNotSupportException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler.SpringBootShutdownHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ShutdownHandlerConfiguration {

    private static final String SIGNAL_TPYE = "USR2";

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initShutdownHandler () {
        try {
            EmbeddedWebApplicationContext embeddedWebApplicationContext = (EmbeddedWebApplicationContext) this.applicationContext;
            SpringBootShutdownHandler springBootShutdownHandler = new SpringBootShutdownHandler(embeddedWebApplicationContext);

            ServerSignalHandler.registShutdownHandler(SIGNAL_TPYE, springBootShutdownHandler);
        } catch (SignalNotSupportException e) {
            System.out.println("The illuminati grace shutdown filter is failed to initialize.");
        } catch (RequiredValueException e) {
            System.out.println("check your springBootShutdownHandler.");
        }
    }
}
