package com.jygoh.jyso.global.auth.controller;

import com.jygoh.jyso.domain.member.dto.LoginRequestDto;
import com.jygoh.jyso.domain.member.dto.RegisterRequestDto;
import com.jygoh.jyso.global.auth.service.AuthService;
import com.jygoh.jyso.global.security.jwt.TokenDto;
import com.jygoh.jyso.global.security.jwt.dto.RefreshTokenRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto requestDto) {
        authService.register(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        TokenDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok().body(tokenDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        TokenDto tokenDto = authService.refreshToken(requestDto.getRefreshToken());
        return ResponseEntity.ok().body(tokenDto);
    }
}
