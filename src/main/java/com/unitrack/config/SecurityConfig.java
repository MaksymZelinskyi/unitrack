package com.unitrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        JwtUsernamePasswordAuthFilter authFilter = new JwtUsernamePasswordAuthFilter(authenticationManager(authConfig), jwtService);
        return http.authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/styles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/collaborators/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/projects/new").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/skills").hasRole("ADMIN")
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated())
                .csrf(x -> x.disable())
                .formLogin(f -> f.loginPage("/login")
                                .defaultSuccessUrl("/home", true)
                                .permitAll()
                                .failureUrl("/login?error=true")
                                .loginProcessingUrl("/process-login")
                )
                .logout(x ->
                        x.logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID"))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(authFilter)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}