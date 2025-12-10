package com.w.backend.domain.user.controller;

import com.w.backend.domain.user.dto.UserJoinRequest;
import com.w.backend.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> join(UserJoinRequest request) {
        userService.join(request);
        return ResponseEntity.ok().build();
    }

}
