package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.RequiredValueException;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

public class SpringBootShutdownHandler implements ShutdownHandler {

    private ServletWebServerApplicationContext embeddedWebApplicationContext;

    public SpringBootShutdownHandler (ServletWebServerApplicationContext embeddedWebApplicationContext) throws RequiredValueException {
        if (embeddedWebApplicationContext == null) {
            throw new RequiredValueException("embeddedWebApplicationContext is required.");
        }
        this.embeddedWebApplicationContext = embeddedWebApplicationContext;
    }

    @Override
    public void stopApplication() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@ Your Application will be stopped soon.            @");
        System.out.println("@ ================================================= @");

        this.embeddedWebApplicationContext.stop();

        System.out.println("@ Your Application is finished. BYE BYE.            @");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    @Override
    public boolean isFinished() {
        return this.embeddedWebApplicationContext.isRunning() == false;
    }
}
