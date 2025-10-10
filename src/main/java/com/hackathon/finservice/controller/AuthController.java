package com.hackathon.finservice.controller;

import com.hackathon.finservice.dto.AuthRequest;
import com.hackathon.finservice.dto.AuthResponse;
import com.hackathon.finservice.dto.LogoutResponse;
import com.hackathon.finservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        String token = authService.authenticateUser(authRequest.identifier(), authRequest.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String token) {
        authService.revokeToken(token);
        return ResponseEntity.ok(new LogoutResponse("Logout successful"));
    }
}
