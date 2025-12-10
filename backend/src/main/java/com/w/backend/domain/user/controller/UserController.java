package com.w.backend.domain.user.controller;

import com.w.backend.domain.user.dto.UserJoinRequest;
import com.w.backend.domain.user.dto.UserResponse;
import com.w.backend.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> join(@RequestBody UserJoinRequest request) {
        userService.join(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        // 방어코드
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        // 정상흐름
        UserResponse response = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}
