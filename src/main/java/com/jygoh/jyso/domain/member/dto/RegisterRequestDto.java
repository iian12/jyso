package com.jygoh.jyso.domain.member.dto;

import com.jygoh.jyso.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestDto {

    private String email;

    private String password;

    private String nickname;

    @Builder
    public RegisterRequestDto(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
