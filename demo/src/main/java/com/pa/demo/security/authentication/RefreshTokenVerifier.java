package com.pa.demo.security.authentication;

import org.springframework.stereotype.Component;

@Component("refreshTokenVerifier")
public class RefreshTokenVerifier implements TokenVerifier {

  @Override
  public boolean verify(String jti) {
    return true;
  }
}
