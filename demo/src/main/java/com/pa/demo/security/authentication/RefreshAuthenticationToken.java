package com.pa.demo.security.authentication;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

public class RefreshAuthenticationToken extends AbstractJwtAuthenticationToken {
  private static final long serialVersionUID = 1L;

  public RefreshAuthenticationToken(RawJwtToken unsafeToken) {
    super(unsafeToken);
  }

  public RefreshAuthenticationToken(AuthenticationUser authenticationUser,
      Collection<? extends GrantedAuthority> authorities) {
    super(authenticationUser, authorities);
  }
}
