package com.w.backend.domain.user.service;

import com.w.backend.domain.user.dto.UserJoinRequest;
import com.w.backend.domain.user.dto.UserResponse;
import com.w.backend.domain.user.entity.User;
import com.w.backend.domain.user.mapper.UserMapper;
import com.w.backend.global.error.CustomException;
import com.w.backend.global.error.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void join(UserJoinRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.create(request.username(), encodedPassword);
        userMapper.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userMapper.findByUsername(username)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

}
