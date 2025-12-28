package com.w.backend.domain.auth.service;

import com.w.backend.domain.auth.dto.RefreshToken;
import com.w.backend.domain.auth.dto.TokenDto;
import com.w.backend.domain.auth.dto.UserLoginRequest;
import com.w.backend.domain.auth.mapper.RefreshTokenMapper;
import com.w.backend.global.error.CustomException;
import com.w.backend.global.error.ErrorCode;
import com.w.backend.global.jwt.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenMapper refreshTokenMapper;

    public AuthService(
            TokenProvider tokenProvider,
            AuthenticationManager authenticationManager,
            RefreshTokenMapper refreshTokenMapper) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Transactional
    public TokenDto login(UserLoginRequest request) {
        Authentication authentication = authenticateUser(request);
        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        saveOrUpdateRefreshToken(authentication.getName(), refreshToken);

        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional
    public TokenDto reissue(TokenDto tokenDto) {
        validateRefreshToken(tokenDto.refreshToken());

        Authentication authentication = tokenProvider.getAuthentication(tokenDto.accessToken());
        RefreshToken storedToken = getStoredRefreshToken(authentication.getName());

        validateTokenMatch(storedToken, tokenDto.refreshToken());

        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        updateRefreshToken(storedToken, refreshToken);

        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional
    public void logout(String username) {
        refreshTokenMapper.deleteByKey(username);
    }

    private Authentication authenticateUser(UserLoginRequest request) {
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private void saveOrUpdateRefreshToken(String key, String value) {
        Optional<RefreshToken> distinctToken = refreshTokenMapper.findByKey(key);
        if (distinctToken.isPresent()) {
            refreshTokenMapper.update(distinctToken.get().updateValue(value));
        } else {
            refreshTokenMapper.save(new RefreshToken(key, value));
        }
    }

    private void validateRefreshToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    private RefreshToken getStoredRefreshToken(String key) {
        return refreshTokenMapper.findByKey(key)
            .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    private void validateTokenMatch(RefreshToken storedToken, String requestToken) {
        if (!storedToken.value().equals(requestToken)) {
            throw new CustomException(ErrorCode.TOKEN_USER_MISMATCH);
        }
    }

    private void updateRefreshToken(RefreshToken storedToken, String newValue) {
        refreshTokenMapper.update(storedToken.updateValue(newValue));
    }

}
