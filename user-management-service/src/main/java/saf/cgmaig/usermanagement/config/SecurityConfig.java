package saf.cgmaig.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Actuator endpoints
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/actuator/**").hasAuthority("ACTUATOR_ACCESS")
                
                // Public endpoints
                .requestMatchers(HttpMethod.GET, "/api/users/dependencias").permitAll()
                
                // User endpoints
                .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("USER_CREATE")
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority("USER_READ")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAuthority("USER_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("USER_DELETE")
                
                // Role endpoints
                .requestMatchers(HttpMethod.POST, "/api/roles").hasAuthority("ROLE_CREATE")
                .requestMatchers(HttpMethod.GET, "/api/roles/**").hasAuthority("ROLE_READ")
                .requestMatchers(HttpMethod.PUT, "/api/roles/**").hasAuthority("ROLE_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasAuthority("ROLE_DELETE")
                
                // Permission endpoints
                .requestMatchers(HttpMethod.GET, "/api/permissions/**").hasAuthority("PERMISSION_READ")
                .requestMatchers(HttpMethod.POST, "/api/permissions").hasAuthority("PERMISSION_CREATE")
                .requestMatchers(HttpMethod.PUT, "/api/permissions/**").hasAuthority("PERMISSION_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/api/permissions/**").hasAuthority("PERMISSION_DELETE")
                
                // Admin operations
                .requestMatchers("/api/*/admin/**").hasAuthority("USER_ADMIN")
                .requestMatchers("/api/*/block", "/api/*/unblock").hasAuthority("USER_ADMIN")
                .requestMatchers("/api/*/reactivate").hasAuthority("USER_ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        converter.setPrincipalClaimName("preferred_username");

        return converter;
    }
}