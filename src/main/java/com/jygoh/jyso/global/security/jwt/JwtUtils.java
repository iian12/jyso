package com.jygoh.jyso.global.security.jwt;

import com.jygoh.jyso.domain.member.entity.Member;
import com.jygoh.jyso.domain.member.repository.MemberRepository;
import com.jygoh.jyso.domain.member.service.UserDetailsServiceImpl;
import com.jygoh.jyso.global.security.jwt.entity.RefreshToken;
import com.jygoh.jyso.global.security.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class JwtUtils {

    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public JwtUtils(UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access")
                ? request.getHeader(jwtProvider.getAccessTokenHeader())
                : request.getHeader(jwtProvider.getRefreshTokenHeader());
    }

    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtProvider.getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public Boolean refreshTokenValidation(String token) {
        if (!tokenValidation(token)) return false;

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail(getEmailFromToken(token));
        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtProvider.getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Member loadMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
