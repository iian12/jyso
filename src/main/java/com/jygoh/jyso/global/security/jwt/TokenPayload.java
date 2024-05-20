package com.jygoh.jyso.global.security.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenPayload {

    private String email;
    private String role;
    private String nickname;

    @Builder
    public TokenPayload(String email, String role, String nickname) {
        this.email = email;
        this.role = role;
        this.nickname = nickname;
    }
}
