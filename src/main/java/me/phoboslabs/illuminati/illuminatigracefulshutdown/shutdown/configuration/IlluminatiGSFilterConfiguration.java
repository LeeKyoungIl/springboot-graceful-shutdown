package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.ApplicationContextBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class IlluminatiGSFilterConfiguration extends OncePerRequestFilter {

    private static final String SHUTDOWN_MESSAGE = "The application is preparing to shutdown.";
    private static final AtomicLong WORK_COUNT = new AtomicLong(0L);
    private static final AtomicBoolean READY_TO_SHUTDOWN = new AtomicBoolean(false);

    private final ApplicationContext applicationContext;

    public IlluminatiGSFilterConfiguration (ApplicationContext applicationContext) throws ApplicationContextBeanException {
        if (applicationContext == null) {
            throw new ApplicationContextBeanException();
        }
        this.applicationContext = applicationContext;
        this.init();
    }

    private void init () {
        ShutdownHandlerConfiguration shutdownHandlerConfiguration = new ShutdownHandlerConfiguration(applicationContext);
        if (shutdownHandlerConfiguration.isInitialized()) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("@ The illuminati graceful shutdown filter is now initialized. @");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (READY_TO_SHUTDOWN.get() == false) {
            WORK_COUNT.set(WORK_COUNT.get()+1L);
            try {
                chain.doFilter(request, response);
            } finally {
                WORK_COUNT.set(WORK_COUNT.get()-1L);
            }
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, SHUTDOWN_MESSAGE);
        }
    }

    @Override
    public void destroy() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@ The illuminati graceful shutdown is completed.              @");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public static long getWorkCount () {
        return WORK_COUNT.get();
    }

    public static void setReadyToShutdown (String signalName) {
        if (signalName.equalsIgnoreCase("TERM")) {
            READY_TO_SHUTDOWN.set(true);
        }
    }
}
