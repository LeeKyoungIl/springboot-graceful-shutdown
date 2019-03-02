package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;

import org.springframework.context.annotation.Configuration;
import sun.misc.Signal;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class ServerSignalFilter implements Filter {

    private static final String SHUTDOWN_MESSAGE = "The application is preparing to shutdown.";
    private static final AtomicLong WORK_COUNT = new AtomicLong(0);
    private static final AtomicBoolean READY_TO_SHUTDOWN = new AtomicBoolean(false);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (READY_TO_SHUTDOWN.get() == false) {
            WORK_COUNT.set(WORK_COUNT.get()+1);
            chain.doFilter(request, response);
            WORK_COUNT.set(WORK_COUNT.get()-1);
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, SHUTDOWN_MESSAGE);
        }
    }

    @Override
    public void destroy() {
        System.out.println("The illuminati Graceful shutdown filter is Activated.");
    }

    public static long getWorkCount () {
        return WORK_COUNT.get();
    }

    public static void setReadyToShutdown (String signalName) {
        System.out.println("shutdown check 1");
        if (signalName.equalsIgnoreCase("TERM")) {
            System.out.println("shutdown check");
            READY_TO_SHUTDOWN.set(true);
        }
    }
}
