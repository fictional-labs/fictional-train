package com.w.backend.domain.user.dto;

import com.w.backend.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보 응답 DTO")
public record UserResponse(
    @Schema(description = "사용자 ID", example = "tester")
    String username
) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getUsername()
        );
    }
    
}
