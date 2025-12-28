package com.w.backend.domain.user.mapper;

import com.w.backend.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void save(User user);
    Optional<User> findByUsername(String username);

}
