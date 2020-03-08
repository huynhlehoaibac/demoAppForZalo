package com.pa.demo.security.authentication;

public enum Scopes {
  REFRESH_TOKEN,
  EXCHANGE_TOKEN;

  public String authority() {
    return "ROLE_" + this.name();
  }
}
