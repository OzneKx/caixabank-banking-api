package com.hackathon.finservice.service;

import com.hackathon.finservice.dto.user.UserRequest;
import com.hackathon.finservice.dto.user.UserResponse;

public interface UserService {
    UserResponse registerUserWithMainAccount(UserRequest userRequest);
}
