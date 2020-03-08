package com.pa.demo.security.authentication;

import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.BadCredentialsException;
import com.pa.demo.security.authentication.JwtToken;
import com.pa.demo.security.authentication.RawJwtToken;
import com.pa.demo.security.authentication.Scopes;
import com.pa.demo.security.core.ExpiredJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class RefreshToken implements JwtToken {
  private static final long serialVersionUID = 1L;

  private transient Jws<Claims> claims;

  private RefreshToken(Jws<Claims> claims) {
    this.claims = claims;
  }

  /**
   * Creates and validates Refresh token
   * 
   * @param token
   * @param signingKey
   * 
   * @throws BadCredentialsException
   * @throws ExpiredJwtException
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public static Optional<RefreshToken> create(RawJwtToken token, String signingKey) {
    Jws<Claims> claims = token.parseClaims(signingKey);

    List<String> scopes = claims.getBody().get("scopes", List.class);
    if (scopes == null || scopes.isEmpty()
        || scopes.stream().noneMatch(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope))) {
      return Optional.empty();
    }

    return Optional.ofNullable(new RefreshToken(claims));
  }

  @Override
  public String getToken() {
    return null;
  }

  public Jws<Claims> getClaims() {
    return claims;
  }

  public String getJti() {
    return claims.getBody().getId();
  }

  public String getSubject() {
    return claims.getBody().getSubject();
  }
}
