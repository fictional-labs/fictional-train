package com.w.backend.domain.auth.mapper;

import com.w.backend.domain.auth.dto.RefreshToken;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {

    void save(RefreshToken refreshToken);
    void update(RefreshToken refreshToken);
    Optional<RefreshToken> findByKey(String key);
    void deleteByKey(String key);
    
}
