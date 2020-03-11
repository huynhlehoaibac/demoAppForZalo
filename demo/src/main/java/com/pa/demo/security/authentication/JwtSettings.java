package com.pa.demo.security.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "demo.security.jwt")
public class JwtSettings {
  /**
   * {@link JwtToken} will expire after this time.
   */
  private Integer tokenExpirationTime;

  /**
   * Token issuer.
   */
  private String tokenIssuer;

  /**
   * Key is used to sign {@link JwtToken}.
   */
  private String tokenSigningKey;

  /**
   * {@link JwtToken} can be refreshed during this timeframe.
   */
  private Integer refreshTokenExpTime;
}
