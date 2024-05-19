package com.jygoh.jyso.member;

import com.jygoh.jyso.domain.member.dto.LoginRequestDto;
import com.jygoh.jyso.domain.member.dto.RegisterRequestDto;
import com.jygoh.jyso.domain.member.entity.Member;
import com.jygoh.jyso.domain.member.repository.MemberRepository;
import com.jygoh.jyso.domain.member.service.MemberService;
import com.jygoh.jyso.global.error.EmailAlreadyExistsException;
import com.jygoh.jyso.global.security.jwt.TokenDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void registerTest() {
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("nickname")
                .build();

        memberService.register(requestDto);

        Member savedMember = memberRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(savedMember);
        assertEquals("test@example.com", savedMember.getEmail());
        assertEquals("nickname", savedMember.getNickname());
        assertNotEquals("password123", savedMember.getPassword());
        assertTrue(passwordEncoder.matches("password123", savedMember.getPassword()));
    }

    @Test
    void loginTest() {
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("nickname")
                .build();

        memberService.register(requestDto);

        // Create login request
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        // Perform login
        TokenDto tokenDto = memberService.login(loginRequest);

        assertNotNull(tokenDto);
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    void registerWithExistingEmailTest() {
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("nickname")
                .build();

        memberService.register(requestDto);

        RegisterRequestDto duplicateRequestDto = RegisterRequestDto.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("nickname")
                .build();

        assertThrows(EmailAlreadyExistsException.class, () -> memberService.register(duplicateRequestDto));
    }

    @Test
    void loginWithInvalidPasswordTest() {
        // Register a new member
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("nickname")
                .build();
        memberService.register(registerRequest);

        // Create login request with invalid password
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();

        assertThrows(BadCredentialsException.class, () -> memberService.login(loginRequest));
    }
}
