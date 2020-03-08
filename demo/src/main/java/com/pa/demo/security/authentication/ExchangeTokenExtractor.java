package com.pa.demo.security.authentication;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.pa.demo.security.config.WebSecurityConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("exchangeTokenExtractor")
public class ExchangeTokenExtractor implements TokenExtractor {
  public static final String HEADER_PREFIX = "Bearer ";

  @Override
  public Authentication extract(HttpServletRequest request) {
    String tokenPayload = request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME);
    if (StringUtils.isEmpty(tokenPayload)) {
      log.debug("Authorization header cannot be empty");

      throw new AuthenticationServiceException("Authorization header cannot be empty");
    }

    if (tokenPayload.length() < HEADER_PREFIX.length()) {
      log.debug("Invalid authorization header size");

      throw new AuthenticationServiceException("Invalid authorization header size");
    }

    String token = tokenPayload.substring(HEADER_PREFIX.length());
    RawJwtToken rawAccessJwtToken = new RawJwtToken(token);
    return new ExchangeTokenAuthenticationToken(rawAccessJwtToken);
  }
}
