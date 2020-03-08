package com.pa.demo.common.repository;

import java.util.Optional;
import com.pa.demo.common.entity.User;

public interface UserRepository extends GenericRepository<User, Integer> {

  Optional<User> findByUid(String uid);
}
