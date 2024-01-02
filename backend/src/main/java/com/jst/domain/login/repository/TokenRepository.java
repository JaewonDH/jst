package com.jst.domain.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity,String> {
    public  Optional<TokenEntity> findByAccessToken(String accessToken);
}
