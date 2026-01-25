package com.unitrack.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.crypto.spec.SecretKeySpec;

import static org.mockito.Mockito.mock;

@Slf4j
public class TestConfig {

    @Bean
    @ServiceConnection(name = "postgres")
    public static PostgreSQLContainer<?> postgreSQLContainer(
            @Value("${POSTGRES_CONTAINER_VERSION}") String containerVersion,
            @Value("${POSTGRES_CONTAINER_USERNAME}") String containerUsername,
            @Value("${POSTGRES_CONTAINER_PASSWORD}") String containerPassword) {

        final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(containerVersion)
                .withUsername(containerUsername)
                .withPassword(containerPassword)
                .withInitScript("testdata/init-role.sql");

        container.start();

        log.info("PostgreSQL container started on port: {}",
                container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
        return container;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String secretKey = "Q29uZ3JhdHVsYXRpb25zISBZb3UndmUgZ2VuZXJhdGVkIGEgc2VjdXJlIGtleSE=";
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secretKey.getBytes(), "HMACSHA256")).build();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return mock(JavaMailSender.class);
    }
}
