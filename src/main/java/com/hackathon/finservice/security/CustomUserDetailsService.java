package com.hackathon.finservice.security;

import com.hackathon.finservice.data.entity.User;
import com.hackathon.finservice.data.repository.UserRepository;
import com.hackathon.finservice.exception.InvalidEmailException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws InvalidEmailException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidEmailException(email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }
}
