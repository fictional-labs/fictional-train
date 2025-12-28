package com.w.backend.domain.auth.controller;

import com.w.backend.domain.auth.dto.TokenDto;
import com.w.backend.domain.auth.dto.UserLoginRequest;
import com.w.backend.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 및 토큰 관리")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "로그인", description = "사용자 ID와 비밀번호를 검증하여 Access/Refresh Token을 발급합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공 (토큰 발급)")
    @ApiResponse(responseCode = "401", description = "자격 증명 실패")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginRequest request) {
        TokenDto tokenDto = authService.login(request);
        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "토큰 재발급", description = "만료된 Access Token을 Refresh Token을 통해 갱신합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    @ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenDto) {
        TokenDto newTokenDto = authService.reissue(tokenDto);
        return ResponseEntity.ok(newTokenDto);
    }

    @Operation(summary = "로그아웃", description = "서버에서 Refresh Token을 삭제합니다. Access Token이 필요합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        authService.logout(username);
        return ResponseEntity.ok().build();
    }

}
