package com.jygoh.jyso.domain.member.controller;

import com.jygoh.jyso.domain.member.dto.LoginRequestDto;
import com.jygoh.jyso.domain.member.dto.RegisterRequestDto;
import com.jygoh.jyso.domain.member.service.MemberService;
import com.jygoh.jyso.global.error.EmailAlreadyExistsException;
import com.jygoh.jyso.global.security.jwt.TokenDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto requestDto) {
        try {
            memberService.register(requestDto);
            return ResponseEntity.ok().build();
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto requestDto) {
        try {
            TokenDto tokenDto = memberService.login(requestDto);
            return ResponseEntity.ok(tokenDto);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to authenticate user.");
        }
    }
}
