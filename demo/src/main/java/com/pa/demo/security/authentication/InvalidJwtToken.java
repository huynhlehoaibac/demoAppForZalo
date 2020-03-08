package com.pa.demo.security.authentication;

import org.springframework.security.authentication.InsufficientAuthenticationException;

public class InvalidJwtToken extends InsufficientAuthenticationException {
  private static final long serialVersionUID = 1L;

  public InvalidJwtToken(String msg) {
    super(msg);
  }
}
