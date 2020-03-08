package com.pa.demo.common.service;

import com.pa.demo.common.dto.UserDTO;
import com.pa.demo.security.core.userdetails.AuthenticationUser;

public interface UserService {

  AuthenticationUser addUser(UserDTO userDTO);
}
