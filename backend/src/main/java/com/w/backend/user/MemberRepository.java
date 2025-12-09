package com.w.backend.user;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

    Member findByUsername(String username);
    
}
