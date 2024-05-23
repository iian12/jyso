package com.jygoh.jyso.global.auth.service;

import com.jygoh.jyso.domain.member.dto.LoginRequestDto;
import com.jygoh.jyso.domain.member.dto.RegisterRequestDto;
import com.jygoh.jyso.global.security.jwt.TokenDto;

public interface AuthService {
    void register(RegisterRequestDto requestDto);
    TokenDto login(LoginRequestDto requestDto);
    TokenDto refreshToken(String refreshToken);
}
