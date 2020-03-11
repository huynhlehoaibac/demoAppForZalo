package com.pa.demo.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import com.pa.demo.security.core.userdetails.AuthenticationUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
  private JwtSettings jwtSettings;
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RawJwtToken rawAccessToken = (RawJwtToken) authentication.getCredentials();

    Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
    Claims claims = jwsClaims.getBody();

    String username = claims.getSubject();

    AuthenticationUser authenticationUser =
        (AuthenticationUser) userDetailsService.loadUserByUsername(username);

    return new JwtAuthenticationToken(authenticationUser, authenticationUser.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @Autowired
  public void setJwtSettings(JwtSettings jwtSettings) {
    this.jwtSettings = jwtSettings;
  }

  @Autowired
  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}
