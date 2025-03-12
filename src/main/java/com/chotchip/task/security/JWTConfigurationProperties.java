package com.chotchip.task.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for jwt authorization.
 * <p>
 * This class is used to bind JWT-related properties from the application's configuration file.
 * It provides essential settings such as the secret key for signing tokens and the expiration time.
 * </p>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JWTConfigurationProperties {
    /**
     * Secret key used for signing and verifying JWT tokens.
     */
    private String secretKey;
    /**
     * Expiration time of JWT tokens in milliseconds.
     * Defines how long a token remains valid before requiring re-authentication.
     */
    private String expirationTime;
}
