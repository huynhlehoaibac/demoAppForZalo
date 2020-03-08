package com.pa.demo.security.authentication;

import io.jsonwebtoken.Claims;

public class AccessJwtToken implements JwtToken {
  private static final long serialVersionUID = 1L;

  private final String rawToken;
  private transient Claims claims;

  protected AccessJwtToken(final String token, Claims claims) {
    this.rawToken = token;
    this.claims = claims;
  }

  public String getToken() {
    return rawToken;
  }

  public Claims getClaims() {
    return claims;
  }
}
