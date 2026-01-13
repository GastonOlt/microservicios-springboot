package com.gaston.springcloud.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SampleGlobalFilter implements GlobalFilter,Ordered {

    private final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("ejecutando el filtro antes del request PRE");

        ServerHttpRequest requestMutated = exchange.getRequest().mutate()
                 .header("token", "12345")
                 .build();

        ServerWebExchange exchangeMutated = exchange.mutate()
                 .request(requestMutated)
                 .build();

        return chain.filter(exchangeMutated).then(Mono.fromRunnable(() -> {
            logger.info("ejecutando el filtro despues del request POST");

            Optional.ofNullable(exchangeMutated.getRequest().getHeaders().getFirst("token")).ifPresent(token -> {
                logger.info("token en response post filter: " + token);
                exchangeMutated.getResponse().getHeaders().add("token", token);
            });

            exchangeMutated.getResponse().getCookies().add("color",ResponseCookie.from("color", "red").build());
            // exchangeMutated.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
      return 100;
    }
}