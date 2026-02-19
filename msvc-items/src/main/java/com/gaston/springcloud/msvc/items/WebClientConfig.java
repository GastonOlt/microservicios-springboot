package com.gaston.springcloud.msvc.items;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // @Bean
    // @LoadBalanced    
    // public WebClient.Builder webClientBuilder() {
    //     return WebClient.builder().baseUrl("http://msvc-products");
    // }

    @Bean
    WebClient webClient(WebClient.Builder webClientBuilder,
                        ReactorLoadBalancerExchangeFilterFunction lbFunction){
        return webClientBuilder.baseUrl("http://msvc-products").filter(lbFunction).build();
    }

}
