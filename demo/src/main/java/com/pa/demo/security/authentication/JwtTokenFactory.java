package com.pa.demo.security.authentication;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.pa.demo.security.core.userdetails.AuthenticationUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenFactory {
  private final JwtSettings settings;

  @Autowired
  public JwtTokenFactory(JwtSettings settings) {
    this.settings = settings;
  }

  /**
   * Factory method for issuing new JWT Tokens.
   * 
   * @param username
   * @param roles
   * @return
   */
  public AccessJwtToken createAccessJwtToken(AuthenticationUser authenticationUser) {
    if (StringUtils.isEmpty(authenticationUser.getUsername())) {
      throw new IllegalArgumentException("Cannot create JWT Token without username");
    }

    Claims claims = Jwts.claims().setSubject(authenticationUser.getUsername());
    claims.put("userId", authenticationUser.getUserId());
    claims.put("uid", authenticationUser.getUid());
    claims.put("name", authenticationUser.getName());
    claims.put("scopes", authenticationUser.getAuthorities().stream().map(Object::toString)
        .collect(Collectors.toList()));

    Date currentTime = new Date();
    String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
        .setIssuedAt(currentTime)
        .setExpiration(DateUtils.addMinutes(currentTime, settings.getTokenExpirationTime()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();

    return new AccessJwtToken(token, claims);
  }

  public JwtToken createRefreshToken(AuthenticationUser authenticationUser) {
    if (StringUtils.isEmpty(authenticationUser.getUsername())) {
      throw new IllegalArgumentException("Cannot create JWT Token without username");
    }

    Claims claims = Jwts.claims().setSubject(authenticationUser.getUsername());
    claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

    Date currentTime = new Date();
    String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
        .setIssuedAt(currentTime)
        .setExpiration(DateUtils.addMinutes(currentTime, settings.getRefreshTokenExpTime()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();

    return new AccessJwtToken(token, claims);
  }

  public JwtToken createExchangeToken(String jit, AuthenticationUser authenticationUser) {
    Date currentTime = new Date();

    Claims claims = Jwts.claims().setSubject(authenticationUser.getUsername()).setId(jit);
    claims.put("scopes", Arrays.asList(Scopes.EXCHANGE_TOKEN.authority()));

    String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
        .setIssuedAt(currentTime).setExpiration(DateUtils.addMinutes(currentTime, 5))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();

    return new AccessJwtToken(token, claims);
  }
}
