package com.unitrack.config;

import com.unitrack.controller.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtService jwtService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final CollaboratorOidcUserService oidcUserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        JwtUsernamePasswordAuthFilter authFilter = new JwtUsernamePasswordAuthFilter(authenticationManager(authConfig), jwtService);
        http.authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/styles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/icons/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/collaborators/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/projects/new").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/projects/new").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/skills/**").denyAll()
                        .requestMatchers(HttpMethod.POST, "/skills/**").denyAll()
                        .requestMatchers(HttpMethod.GET, "/skills/**").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/password/**").permitAll()
                        .requestMatchers("/.well-known/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(x -> x.disable())
                .formLogin(f -> f.loginPage("/login")
                                .defaultSuccessUrl("/home", true)
                                .failureUrl("/login?error=true")
                                .loginProcessingUrl("/process-login")
                                .successHandler(loginSuccessHandler)
                                .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(loginSuccessHandler)
                        )
                .logout(x ->
                        x.logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID"))
                .addFilter(authFilter);

        return http.build();
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