package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.ServerSignalHandler;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.RequiredValueException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.SignalNotSupportException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler.SpringBootShutdownHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ShutdownHandlerConfiguration {

    private static final String SIGNAL_TYPE = "TERM";

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initShutdownHandler () {
        try {
            ServletWebServerApplicationContext embeddedWebApplicationContext = (ServletWebServerApplicationContext) this.applicationContext;
            SpringBootShutdownHandler springBootShutdownHandler = new SpringBootShutdownHandler(embeddedWebApplicationContext);

            ServerSignalHandler.registShutdownHandler(SIGNAL_TYPE, springBootShutdownHandler);
        } catch (SignalNotSupportException sigex) {
            System.out.println("The illuminati grace shutdown filter is failed to initialize.");
            sigex.printStackTrace();
        } catch (RequiredValueException regex) {
            System.out.println("check your springBootShutdownHandler.");
            regex.printStackTrace();
        }
    }
}
