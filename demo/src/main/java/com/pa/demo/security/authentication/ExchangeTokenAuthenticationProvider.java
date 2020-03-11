package com.pa.demo.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

@Component
public class ExchangeTokenAuthenticationProvider implements AuthenticationProvider {
  private JwtSettings jwtSettings;
  private UserDetailsService userDetailsService;
  private TokenVerifier tokenVerifier;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RawJwtToken rawToken = (RawJwtToken) authentication.getCredentials();

    ExchangeToken exchangeToken = ExchangeToken.create(rawToken, jwtSettings.getTokenSigningKey())
        .orElseThrow(() -> new InvalidJwtToken("Invalid Jwt Token"));

    String jti = exchangeToken.getJti();
    if (!tokenVerifier.verify(jti)) {
      throw new InvalidJwtToken("Invalid jti");
    }

    String username = exchangeToken.getSubject();

    AuthenticationUser authenticationUser =
        (AuthenticationUser) userDetailsService.loadUserByUsername(username);

    return new ExchangeTokenAuthenticationToken(authenticationUser,
        authenticationUser.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (ExchangeTokenAuthenticationToken.class.isAssignableFrom(authentication));
  }

  @Autowired
  public void setJwtSettings(JwtSettings jwtSettings) {
    this.jwtSettings = jwtSettings;
  }

  @Autowired
  @Qualifier("defaultTokenVerifier")
  public void setTokenVerifier(TokenVerifier tokenVerifier) {
    this.tokenVerifier = tokenVerifier;
  }

  @Autowired
  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}
