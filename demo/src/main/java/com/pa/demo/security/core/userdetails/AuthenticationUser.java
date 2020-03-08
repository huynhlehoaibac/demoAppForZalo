package com.pa.demo.security.core.userdetails;

import org.springframework.security.core.authority.AuthorityUtils;
import com.pa.demo.common.entity.User;

public class AuthenticationUser extends org.springframework.security.core.userdetails.User {
  private static final long serialVersionUID = 1L;

  private int userId;
  private String uid;
  private String name;

  public AuthenticationUser(User user) {
    super(user.getUid(), "", true, true, true, true, AuthorityUtils.NO_AUTHORITIES);

    this.userId = user.getUserId();
    this.uid = user.getUid();
    this.name = user.getName();
  }

  public int getUserId() {
    return userId;
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object rhs) {
    if (rhs instanceof User) {
      return uid.equals(((User) rhs).getUid());
    }
    return false;
  }

  /**
   * Returns the hashcode of the {@code emailAddress}.
   */
  @Override
  public int hashCode() {
    return uid.hashCode();
  }
}
