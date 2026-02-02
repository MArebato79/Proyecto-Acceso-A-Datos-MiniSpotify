package com.rebatosoft.minispotify.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider; // Ahora esto se inyecta desde ApplicationConfig

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests(auth -> auth
                            // 1. Rutas Públicas (Login, Registro, Ver canciones)
                            .requestMatchers("/auth/**", "/usuarios/register").permitAll()
                            .requestMatchers(HttpMethod.GET, "/canciones/**", "/albumes/**", "/artistas/**").authenticated() // Todos pueden ver
                            // 2. Rutas SOLO para ARTISTAS (Crear, Modificar, Borrar)
                            .requestMatchers(HttpMethod.POST, "/canciones/**").hasRole("ARTISTA")
                            .requestMatchers(HttpMethod.PUT, "/canciones/**").hasRole("ARTISTA")
                            .requestMatchers(HttpMethod.DELETE, "/canciones/**").hasRole("ARTISTA")

                            .requestMatchers(HttpMethod.POST, "/albumes/**").hasRole("ARTISTA")
                            .requestMatchers(HttpMethod.PUT, "/albumes/**").hasRole("ARTISTA")
                            .requestMatchers(HttpMethod.DELETE, "/albumes/**").hasRole("ARTISTA")

                            // 3. El resto requiere estar autenticado (usuarios normales)
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOrigins(List.of("http://localhost:5173"));

            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
    }

