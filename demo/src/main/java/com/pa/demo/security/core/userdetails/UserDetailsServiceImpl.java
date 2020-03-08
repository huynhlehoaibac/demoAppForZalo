package com.pa.demo.security.core.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.pa.demo.common.entity.User;
import com.pa.demo.common.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Value("${account-validity-period:180}")
  private int accountValidityPeriod;

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
    User user = userRepository.findByUid(uid)
        .orElseThrow(() -> new UsernameNotFoundException("Username: " + uid + " not found"));

    return new AuthenticationUser(user);
  }
}
