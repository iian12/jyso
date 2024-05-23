package com.jygoh.jyso.global.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JwtUtils {

    private final JwtProvider jwtProvider;

    public JwtUtils(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String getEmailFromToken(String token) {
        Claims claims = jwtProvider.getClaimsFromToken(token);
        return claims.getSubject();
    }

    public String getUserRoleFromToken(String token) {
        Claims claims = jwtProvider.getClaimsFromToken(token);
        return (String) claims.get("role");
    }

    public String getUserNicknameFromToken(String token) {
        Claims claims = jwtProvider.getClaimsFromToken(token);
        return (String) claims.get("nickname");
    }
}
