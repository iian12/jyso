package com.jygoh.jyso.global.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.token.expiration}")
    private long accessTime;

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTime;

    @Getter
    @Value("${jwt.access.token.header}")
    public String accessTokenHeader;

    @Getter
    @Value("${jwt.refresh.token.header}")
    public String refreshTokenHeader;

    @Getter
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto createAllToken(String email, String role, String nickname) {
        return new TokenDto(createToken(email, role, nickname, "Access"), createToken(email, role, nickname, "Refresh"));
    }

    public String createToken(String email, String role, String nickname, String type) {
        Date date = new Date();
        long time = type.equals("Access") ? accessTime : refreshTime;

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("nickname", nickname)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(accessTokenHeader, accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshTokenHeader, refreshToken);
    }
}
