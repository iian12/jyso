package com.jygoh.jyso.domain.member.service;

import com.jygoh.jyso.domain.member.dto.LoginRequestDto;
import com.jygoh.jyso.domain.member.dto.RegisterRequestDto;
import com.jygoh.jyso.global.security.jwt.TokenDto;

public interface MemberService {

    void register(RegisterRequestDto requestDto);
    TokenDto login(LoginRequestDto requestDto);
}
