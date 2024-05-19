package com.jygoh.jyso.domain.member.dto;

import com.jygoh.jyso.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    private String email;

    private String password;

    @Builder
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}
