package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.ApplicationContextBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class IlluminatiGSFilterConfiguration extends OncePerRequestFilter {

    private static final String SHUTDOWN_MESSAGE = "The application is preparing to shutdown.";
    private static final AtomicLong WORK_COUNT = new AtomicLong(0L);
    private static final AtomicBoolean READY_TO_SHUTDOWN = new AtomicBoolean(false);

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    private void init () {
        if (this.applicationContext != null) {
            ShutdownHandlerConfiguration shutdownHandlerConfiguration = new ShutdownHandlerConfiguration(applicationContext);
            if (shutdownHandlerConfiguration.isInitialized()) {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println("@ The illuminati graceful shutdown filter is now initialized. @");
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            }
        } else {
            throw new ApplicationContextBeanException();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (READY_TO_SHUTDOWN.get() == false) {
            WORK_COUNT.set(WORK_COUNT.get()+1L);
            try {
                if (READY_TO_SHUTDOWN.get() == false) {
                    chain.doFilter(request, response);
                } else {
                    this.returnTo503Status(response);
                }
            } finally {
                WORK_COUNT.set(WORK_COUNT.get()-1L);
            }
        } else {
            this.returnTo503Status(response);
        }
    }

    private void returnTo503Status (HttpServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, SHUTDOWN_MESSAGE);
    }

    public static long getWorkCount () {
        return WORK_COUNT.get();
    }

    public static void setReadyToShutdown (String signalName) {
        if (READY_TO_SHUTDOWN.get() == false && signalName.equalsIgnoreCase("TERM")) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("@ Your Application will be stopped soon.            @");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            READY_TO_SHUTDOWN.set(true);
        }
    }

    public static boolean isReadyToShutdown() {
        return READY_TO_SHUTDOWN.get();
    }
}
