package com.rebatosoft.minispotify.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer; // <--- IMPORTANTE
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // <--- IMPORTANTE
import org.springframework.web.cors.CorsConfigurationSource; // <--- IMPORTANTE
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // <--- IMPORTANTE

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/usuarios/register").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/canciones/**", "/albums/**", "/artistas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/canciones/**").hasRole("ARTISTA")
                        .requestMatchers(HttpMethod.PUT, "/canciones/**").hasRole("ARTISTA")
                        .requestMatchers("/usuarios/follow/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/canciones/**").hasRole("ARTISTA")

                        .requestMatchers(HttpMethod.POST, "/albums/**").hasRole("ARTISTA")
                        .requestMatchers(HttpMethod.PUT, "/albums/**").hasRole("ARTISTA")
                        .requestMatchers(HttpMethod.DELETE, "/albums/**").hasRole("ARTISTA")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2. DEFINIR LA CONFIGURACIÓN DE CORS (QUÉ ESTÁ PERMITIDO)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Necesario para que pasen las credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}