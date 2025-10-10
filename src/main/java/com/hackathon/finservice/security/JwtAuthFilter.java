package com.hackathon.finservice.security;

import com.hackathon.finservice.data.entity.Token;
import com.hackathon.finservice.data.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        if (token != null) {
            authenticateToken(token, request);
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        return header.substring(7);
    }

    private void authenticateToken(String token, HttpServletRequest request) {
        try {
            if (checkIfTokenIsRevoked(token)) return;

            String username = jwtUtil.extractUsername(token);
            boolean isContextUnauthenticated = SecurityContextHolder.getContext().getAuthentication() == null;

            if (username != null && isContextUnauthenticated) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            System.out.println("JWT invalid: " + e.getMessage());
        }
    }

    private boolean checkIfTokenIsRevoked(String token) {
        boolean isRevoked = tokenRepository.findByToken(token)
                .map(Token::isRevoked)
                .orElse(true);

        if (isRevoked) {
            System.out.println("Token revoked or invalid: " + token);
            return true;
        }
        return false;
    }
}