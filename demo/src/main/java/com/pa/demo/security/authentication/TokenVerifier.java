package com.pa.demo.security.authentication;

public interface TokenVerifier {
  public boolean verify(String jti);
}
