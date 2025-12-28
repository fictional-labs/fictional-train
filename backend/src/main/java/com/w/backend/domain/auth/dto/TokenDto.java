package com.w.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 응답 DTO")
public record TokenDto(
    @Schema(description = "Access Token (Bearer)")
    String accessToken,

    @Schema(description = "Refresh Token")
    String refreshToken
) {
}
