package com.hackathon.finservice.service;

import com.hackathon.finservice.data.entity.Token;
import com.hackathon.finservice.data.entity.User;
import com.hackathon.finservice.data.repository.TokenRepository;
import com.hackathon.finservice.data.repository.UserRepository;
import com.hackathon.finservice.security.CustomUserDetailsService;
import com.hackathon.finservice.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    public String authenticateUser(String email, String password) {
        User user = findUserByEmail(email);
        validatePassword(password, user);
        String jwt = generateTokenForUser(user);
        saveToken(jwt, user);
        return jwt;
    }

    public void revokeToken(String rawToken) {
        String tokenExtracted = extractToken(rawToken);
        Token token = findToken(tokenExtracted);
        revokeToken(token);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        BAD_REQUEST,
                        "User not found for the given identifier: " + email
                ));
    }

    private void validatePassword(String rawPassword, User user) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Bad credentials");
        }
    }

    private String generateTokenForUser(User user) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        return jwtUtil.generateToken(userDetails);
    }

    private void saveToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setUser(user);
        tokenRepository.save(token);
    }

    private String extractToken(String rawToken) {
        return rawToken.replace("Bearer ", "").trim();
    }

    private Token findToken(String tokenExtracted) {
        return tokenRepository.findByToken(tokenExtracted).orElseThrow(() -> new ResponseStatusException(
                BAD_REQUEST,
                "Invalid token"
        ));
    }

    private void revokeToken(Token token) {
        token.setRevoked(true);
        tokenRepository.save(token);
    }
}
