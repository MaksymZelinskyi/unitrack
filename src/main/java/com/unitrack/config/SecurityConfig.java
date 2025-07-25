package com.unitrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(x -> x
                .requestMatchers(HttpMethod.GET, "/collaborators/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/projects/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/projects/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/skills").hasRole("ADMIN")
                .anyRequest().authenticated())
                .formLogin(f ->
                        f.loginPage("/login")
                                .successForwardUrl("/home")
                                .failureForwardUrl("/login?error=true")
                                .loginProcessingUrl("/process-login")
                )
                .logout(x ->
                        x.logoutSuccessUrl("/login?logout=true"))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
