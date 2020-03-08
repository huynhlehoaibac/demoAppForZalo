package com.pa.demo.common.service.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pa.demo.common.dto.UserDTO;
import com.pa.demo.common.entity.User;
import com.pa.demo.common.mapper.UserMapper;
import com.pa.demo.common.repository.UserRepository;
import com.pa.demo.common.service.UserService;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserMapper userMapper;

  @Override
  public AuthenticationUser addUser(UserDTO userDTO) {
    Date now = new Date();

    // @formatter:off
    User user = User.builder()
        .uid(userDTO.getUid())
        .name(userDTO.getName())
        .insertDate(now)
        .build();
    // @formatter:on

    User savedUser = userRepository.save(user);

    return new AuthenticationUser(savedUser);
  }
}
