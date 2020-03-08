package com.pa.demo.security.authentication;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("defaultTokenVerifier")
public class DefaultTokenVerifier implements TokenVerifier {

  private HttpSession httpSession;

  @Override
  public boolean verify(String jti) {
    if (httpSession == null) {
      return false;
    }

    return jti.equals(httpSession.getAttribute("jti"));
  }

  @Autowired
  public void setHttpSession(HttpSession httpSession) {
    this.httpSession = httpSession;
  }
}
