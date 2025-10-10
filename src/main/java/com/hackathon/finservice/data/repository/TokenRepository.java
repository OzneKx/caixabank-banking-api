package com.hackathon.finservice.data.repository;

import com.hackathon.finservice.data.entity.Token;
import com.hackathon.finservice.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    List<Token> findAllByUser(User user);
}
