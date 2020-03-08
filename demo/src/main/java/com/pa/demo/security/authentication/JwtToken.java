package com.pa.demo.security.authentication;

import java.io.Serializable;

public interface JwtToken extends Serializable {

  String getToken();
}
