package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler;

import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.util.Assert;

public class SpringBootShutdownHandler implements ShutdownHandler {

    private EmbeddedWebApplicationContext embeddedWebApplicationContext;

    public SpringBootShutdownHandler (EmbeddedWebApplicationContext embeddedWebApplicationContext) {
        Assert.notNull(embeddedWebApplicationContext, "embeddedWebApplicationContext is required.");
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
    public boolean isFinishied() {
        return this.embeddedWebApplicationContext.isRunning() == false;
    }
}
