//does the job of creating a web client.builder which is specialized web client for user service
//using this we can make calls to the user service
package com.fitness.gateway.user;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced //allows webclient to resolve the service name via eureka

    public WebClient.Builder webClientBuilder() {

        return WebClient.builder();
    }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder){ //this bean configured to the below user service
        return webClientBuilder
               .baseUrl("http://USER-SERVICE")
               .build();
    }
}
