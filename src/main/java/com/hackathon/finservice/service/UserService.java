package com.hackathon.finservice.service;

import com.hackathon.finservice.data.entity.Account;
import com.hackathon.finservice.data.entity.User;
import com.hackathon.finservice.data.mapper.UserMapper;
import com.hackathon.finservice.data.repository.UserRepository;
import com.hackathon.finservice.dto.UserRequest;
import com.hackathon.finservice.dto.UserResponse;
import com.hackathon.finservice.exception.EmailAlreadyExistsException;
import com.hackathon.finservice.util.PasswordValidator;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUserWithMainAccount(UserRequest userRequest) {
        PasswordValidator.validatePassword(userRequest.getPassword());

        validateEmailUniqueness(userRequest.getEmail());

        User user = prepareUserForRegistration(userRequest);
        Account mainAccount = createMainAccountForUser(user);
        user.setAccounts(List.of(mainAccount));
        userRepository.save(user);

        UserResponse userResponse = userMapper.toResponse(user);
        userResponse.setAccountNumber(mainAccount.getAccountNumber());
        userResponse.setAccountType(mainAccount.getAccountType());
        return userResponse;
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

    private User prepareUserForRegistration(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return user;
    }

    private Account createMainAccountForUser(User user) {
        Account mainAccount = new Account();
        mainAccount.setAccountNumber(generateAccountNumber());
        mainAccount.setAccountType("Main");
        mainAccount.setUser(user);
        return mainAccount;
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
