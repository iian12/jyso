package com.jygoh.jyso.global.auth.service;

import com.jygoh.jyso.domain.member.dto.LoginRequestDto;
import com.jygoh.jyso.domain.member.dto.RegisterRequestDto;
import com.jygoh.jyso.domain.member.entity.Member;
import com.jygoh.jyso.domain.member.repository.MemberRepository;
import com.jygoh.jyso.global.security.jwt.JwtProvider;
import com.jygoh.jyso.global.security.jwt.TokenDto;
import com.jygoh.jyso.global.security.jwt.TokenPayload;
import com.jygoh.jyso.global.security.jwt.entity.RefreshToken;
import com.jygoh.jyso.global.security.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider, BCryptPasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void register(RegisterRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new IllegalArgumentException("Nickname already in use");
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .build();

        memberRepository.save(member);
    }

    @Override
    public TokenDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        TokenPayload payload = TokenPayload.builder()
                .email(member.getEmail())
                .role(member.getRole().name())
                .nickname(member.getNickname())
                .build();

        // 새로운 액세스 토큰 발급
        String accessToken = jwtProvider.generateAccessToken(payload);

        // 새로운 리프레시 토큰 발급
        String refreshToken = jwtProvider.generateRefreshToken(payload);

        // 발급된 리프레시 토큰을 데이터베이스에 저장
        saveRefreshToken(refreshToken, member);

        // 토큰 반환
        return new TokenDto(accessToken, refreshToken);
    }

    @Override
    public TokenDto refreshToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken) || jwtProvider.isRefreshTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        Claims claims = jwtProvider.getClaimsFromToken(refreshToken);
        String email = claims.getSubject();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 새로운 액세스 토큰 생성
        TokenPayload payload = TokenPayload.builder()
                .email(member.getEmail())
                .role(member.getRole().name())
                .nickname(member.getNickname())
                .build();

        String accessToken = jwtProvider.generateAccessToken(payload);

        // 리프레시 토큰은 이미 발급된 것을 사용
        return new TokenDto(accessToken, refreshToken);
    }

    private TokenDto generateToken(Member member) {
        TokenPayload payload = TokenPayload.builder()
                .email(member.getEmail())
                .role(member.getRole().name())
                .nickname(member.getNickname())
                .build();

        String accessToken = jwtProvider.generateAccessToken(payload);
        String refreshToken = jwtProvider.generateRefreshToken(payload);

        return new TokenDto(accessToken, refreshToken);
    }

    private void saveRefreshToken(String refreshToken, Member member) {
        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .member(member)
                .build();
        refreshTokenRepository.save(token);
    }
}
