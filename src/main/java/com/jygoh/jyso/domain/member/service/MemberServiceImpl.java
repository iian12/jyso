package com.jygoh.jyso.domain.member.service;

import com.jygoh.jyso.domain.member.dto.LoginRequestDto;
import com.jygoh.jyso.domain.member.dto.RegisterRequestDto;
import com.jygoh.jyso.domain.member.entity.Member;
import com.jygoh.jyso.domain.member.repository.MemberRepository;
import com.jygoh.jyso.global.error.EmailAlreadyExistsException;
import com.jygoh.jyso.global.security.jwt.JwtProvider;
import com.jygoh.jyso.global.security.jwt.TokenDto;
import com.jygoh.jyso.global.security.jwt.TokenPayload;
import com.jygoh.jyso.global.security.jwt.entity.RefreshToken;
import com.jygoh.jyso.global.security.jwt.repository.RefreshTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder
    ,AuthenticationManager authenticationManager, JwtProvider jwtProvider, RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void register(RegisterRequestDto requestDto) {

        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();

        Boolean isExist = memberRepository.existsByEmail(email);

        if (isExist) {
            throw new EmailAlreadyExistsException("Already Exists : " + email);
        }

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .build();

        memberRepository.save(member);
    }

    @Override
    public TokenDto login(LoginRequestDto requestDto) {

        try {
            // 1. 이메일과 비밀번호로 인증을 시도합니다.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
            );

            // 2. 인증이 성공하면 SecurityContext에 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Not Found : " + requestDto.getEmail()));

            TokenPayload tokenPayload = TokenPayload.builder()
                    .email(requestDto.getEmail())
                    .role(member.getRole().name())
                    .nickname(member.getNickname())
                    .build();


            // 3. JWT를 생성합니다.
            String accessToken = jwtProvider.createToken(tokenPayload, "Access");
            String refreshToken = jwtProvider.createToken(tokenPayload, "Refresh");

            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMemberEmail(requestDto.getEmail());
            if (optionalRefreshToken.isPresent()) {
                RefreshToken existingRefreshToken = optionalRefreshToken.get();
                existingRefreshToken.updateToken(refreshToken);
                refreshTokenRepository.save(existingRefreshToken);
            } else {
                refreshTokenRepository.save(new RefreshToken(refreshToken, requestDto.getEmail()));
            }

            // 5. 생성된 토큰을 반환합니다.
            return new TokenDto(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            // 로그인 실패 시 예외를 잡아서 처리합니다.
            throw new BadCredentialsException("Authentication failed: " + e.getMessage());
        }
    }
}
