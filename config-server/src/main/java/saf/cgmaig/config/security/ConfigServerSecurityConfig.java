package saf.cgmaig.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para Spring Cloud Config Server.
 * 
 * Propósito:
 * - Protege los endpoints del Config Server con autenticación HTTP Basic
 * - Define usuarios para acceso a las configuraciones:
 *   * config-client: Para microservicios que consumen configuraciones
 *   * config-admin: Para administradores que gestionan configuraciones
 * - Permite acceso público solo a endpoints de actuator (health checks)
 * 
 * Seguridad:
 * - Las credenciales están encriptadas con BCrypt
 * - Requiere autenticación para todos los endpoints excepto /actuator/**
 * - Usa roles para controlar el acceso (CONFIG_CLIENT, CONFIG_ADMIN)
 */
@Configuration
@EnableWebSecurity
public class ConfigServerSecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     * Permite acceso público a endpoints de actuator y requiere autenticación para el resto.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Permitir acceso público a endpoints de monitoreo
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                // Requerir autenticación para endpoints de configuración
                .anyRequest().authenticated()
            )
            .httpBasic(); // Usar autenticación HTTP Basic
        
        return http.build();
    }

    /**
     * Define los usuarios que pueden acceder al Config Server.
     * 
     * Usuarios:
     * - config-client: Usado por microservicios para obtener sus configuraciones
     * - config-admin: Usado por administradores para gestionar configuraciones
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Usuario para microservicios clientes
        UserDetails configClient = User.builder()
                .username("config-client")
                .password(passwordEncoder().encode("config-secret"))
                .roles("CONFIG_CLIENT")
                .build();

        // Usuario administrador para gestión de configuraciones
        UserDetails configAdmin = User.builder()
                .username("config-admin")
                .password(passwordEncoder().encode("admin-secret"))
                .roles("CONFIG_ADMIN")
                .build();

        return new InMemoryUserDetailsManager(configClient, configAdmin);
    }

    /**
     * Configurador de encriptación de passwords.
     * Usa BCrypt para hashear las credenciales.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}