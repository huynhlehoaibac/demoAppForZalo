package com.pa.demo.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import com.pa.demo.security.config.JwtSettings;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

@Component
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
  private JwtSettings jwtSettings;
  private UserDetailsService userDetailsService;
  private TokenVerifier tokenVerifier;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RawJwtToken rawToken = (RawJwtToken) authentication.getCredentials();

    RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey())
        .orElseThrow(() -> new InvalidJwtToken("Invalid Jwt Token"));

    String jti = refreshToken.getJti();
    if (!tokenVerifier.verify(jti)) {
      throw new InvalidJwtToken("Invalid jti");
    }

    String username = refreshToken.getSubject();

    AuthenticationUser authenticationUser =
        (AuthenticationUser) userDetailsService.loadUserByUsername(username);

    return new RefreshAuthenticationToken(authenticationUser, authenticationUser.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return RefreshAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @Autowired
  public void setJwtSettings(JwtSettings jwtSettings) {
    this.jwtSettings = jwtSettings;
  }

  @Autowired
  @Qualifier("refreshTokenVerifier")
  public void setTokenVerifier(TokenVerifier tokenVerifier) {
    this.tokenVerifier = tokenVerifier;
  }

  @Autowired
  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}
