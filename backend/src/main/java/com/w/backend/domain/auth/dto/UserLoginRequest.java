package com.w.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 DTO")
public record UserLoginRequest(
    @Schema(description = "사용자 ID", example = "tester")
    String username,

    @Schema(description = "비밀번호", example = "1234")
    String password
) {
}
