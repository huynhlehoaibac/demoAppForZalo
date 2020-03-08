package com.pa.demo.security.authentication;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import com.pa.demo.security.authentication.AbstractJwtAuthenticationToken;
import com.pa.demo.security.authentication.RawJwtToken;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

public class ExchangeTokenAuthenticationToken extends AbstractJwtAuthenticationToken {
  private static final long serialVersionUID = 1L;

  public ExchangeTokenAuthenticationToken(RawJwtToken unsafeToken) {
    super(unsafeToken);
  }

  public ExchangeTokenAuthenticationToken(AuthenticationUser authenticationUser,
      Collection<? extends GrantedAuthority> authorities) {
    super(authenticationUser, authorities);
  }
}
