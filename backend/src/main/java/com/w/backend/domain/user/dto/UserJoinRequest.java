package com.w.backend.domain.user.dto;

import com.w.backend.domain.user.entity.User;

public record UserJoinRequest(
    String username,
    String password
) {

    public User toEntity() {
        return User.create(this.username, this.password);
    }

}
