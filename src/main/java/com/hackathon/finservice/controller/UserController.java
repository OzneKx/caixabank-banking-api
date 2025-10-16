package com.hackathon.finservice.controller;

import com.hackathon.finservice.dto.user.UserRequest;
import com.hackathon.finservice.dto.user.UserResponse;
import com.hackathon.finservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createCustomerAccount(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.registerUserWithMainAccount(userRequest);
        return ResponseEntity.ok(userResponse);
    }
}
