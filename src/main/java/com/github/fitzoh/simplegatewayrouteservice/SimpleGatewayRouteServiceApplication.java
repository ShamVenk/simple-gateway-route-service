package com.github.fitzoh.simplegatewayrouteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import static org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER;

@SpringBootApplication
public class SimpleGatewayRouteServiceApplication {


    public static final String FORWARDED_URL = "X-CF-Forwarded-Url";

    public static final String PROXY_METADATA = "X-CF-Proxy-Metadata";

    public static final String PROXY_SIGNATURE = "X-CF-Proxy-Signature";


    public static void main(String[] args) {
        SpringApplication.run(SimpleGatewayRouteServiceApplication.class, args);
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
                                           SimpleLoggingFilter loggingFilter,
                                           RouteServiceForwardingFilter forwardingFilter) {
        return builder.routes()
                .route(r ->
                        r.header(FORWARDED_URL, ".*")
                                .and()
                                .header(PROXY_METADATA, ".*")
                                .and()
                                .header(PROXY_SIGNATURE, ".*")
                                .filters(f -> {
                                    f.filter(loggingFilter);
                                    f.filter(forwardingFilter, ROUTE_TO_URL_FILTER_ORDER + 1);
                                    return f;
                                })
                                .uri("http://google.com:80"))
                .build();
    }


}
