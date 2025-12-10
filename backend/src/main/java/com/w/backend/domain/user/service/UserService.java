package com.w.backend.domain.user.service;

import com.w.backend.domain.user.dto.UserJoinRequest;
import com.w.backend.domain.user.entity.User;
import com.w.backend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void join(UserJoinRequest request) {
        User user = request.toEntity();
        userRepository.save(user);
    }

}
