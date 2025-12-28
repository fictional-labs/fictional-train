package com.w.backend.domain.auth.dto;

public record RefreshToken(String key, String value) {

    public RefreshToken updateValue(String token) {
        return new RefreshToken(this.key, token);
    }

}
