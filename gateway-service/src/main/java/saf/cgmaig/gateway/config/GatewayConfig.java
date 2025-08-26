package saf.cgmaig.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Route
                .route("auth-service-route", r -> r
                        .path("/auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8081"))
                
                // User Service Route
                .route("user-service-route", r -> r
                        .path("/users/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8082"))
                
                // Request Service Route
                .route("request-service-route", r -> r
                        .path("/requests/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8083"))
                
                // Document Service Route
                .route("document-service-route", r -> r
                        .path("/documents/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8084"))
                
                // Email Service Route
                .route("email-service-route", r -> r
                        .path("/email/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8085"))
                
                // Repository Service Route
                .route("repository-service-route", r -> r
                        .path("/repository/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8086"))
                
                // Virtual Meeting Service Route
                .route("meeting-service-route", r -> r
                        .path("/meetings/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8087"))
                
                // Generic API Route (fallback)
                .route("api-service-route", r -> r
                        .path("/api/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8081"))
                
                .build();
    }
}