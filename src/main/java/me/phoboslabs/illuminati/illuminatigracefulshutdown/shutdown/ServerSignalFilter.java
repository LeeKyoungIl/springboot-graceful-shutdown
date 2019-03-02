package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;

import org.springframework.context.annotation.Configuration;

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
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@ The illuminati graceful shutdown filter is now initialized. @");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
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
