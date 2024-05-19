package com.jygoh.jyso.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtUtils jwtUtils;

    public JwtFilter(JwtProvider jwtProvider, JwtUtils jwtUtils) {
        this.jwtProvider = jwtProvider;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtils.getHeaderToken(request, "Access");
        String refreshToken = jwtUtils.getHeaderToken(request, "Header");

        if (accessToken != null) {
            if (jwtUtils.tokenValidation(accessToken)) {
                setAuthentication(jwtUtils.getEmailFromToken(accessToken));
            } else if (refreshToken != null) {
                if (jwtUtils.refreshTokenValidation(refreshToken)) {
                    String email = jwtUtils.getEmailFromToken(refreshToken);
                    String newAccessToken = jwtProvider.createToken(email, "Access");
                    jwtProvider.setHeaderAccessToken(response, newAccessToken);
                    setAuthentication(jwtUtils.getEmailFromToken(newAccessToken));
                } else {
                    jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String email) {
        Authentication authentication = jwtUtils.createAuthentication(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(msg);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
