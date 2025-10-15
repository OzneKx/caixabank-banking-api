package com.hackathon.finservice.service;

import com.hackathon.finservice.dto.UserRequest;
import com.hackathon.finservice.dto.UserResponse;

public interface UserService {
    UserResponse registerUserWithMainAccount(UserRequest userRequest);
}
