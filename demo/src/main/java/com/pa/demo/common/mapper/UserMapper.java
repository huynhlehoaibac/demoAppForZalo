package com.pa.demo.common.mapper;

import org.springframework.stereotype.Component;
import com.pa.demo.common.dto.UserDTO;
import com.pa.demo.common.entity.User;

@Component
public class UserMapper implements Mapper<User, UserDTO> {

  @Override
  public User toEntity(UserDTO source) {

    if (source == null) {
      return null;
    }

    // @formatter:off
    return User.builder()
        .userId(source.getUserId())
        .uid(source.getUid())
        .name(source.getName())
        .build();
    // @formatter:on
  }

  @Override
  public UserDTO toDTO(User source) {

    if (source == null) {
      return null;
    }

    // @formatter:off
    return UserDTO.builder()
        .userId(source.getUserId())
        .uid(source.getUid())
        .name(source.getName())
        .build();
    // @formatter:on
  }

}
