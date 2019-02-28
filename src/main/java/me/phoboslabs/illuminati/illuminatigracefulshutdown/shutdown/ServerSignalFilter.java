package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class ServerSignalFilter implements Filter {

    private static final AtomicLong WORK_COUNT = new AtomicLong(0);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        WORK_COUNT.set(WORK_COUNT.get()+1);
        System.out.println("input");
        chain.doFilter(request, response);
        System.out.println("output");
        WORK_COUNT.set(WORK_COUNT.get()-1);
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }

    public static long getWorkCount () {
        return WORK_COUNT.get();
    }
}
