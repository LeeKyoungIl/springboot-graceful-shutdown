package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class IlluminatiGSWebFilterConfiguration implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        return null;
    }
}
