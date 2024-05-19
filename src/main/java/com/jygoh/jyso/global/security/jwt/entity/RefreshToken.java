package com.jygoh.jyso.global.security.jwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    private String memberEmail;


    public RefreshToken(String token, String email) {
        this.refreshToken = token;
        this.memberEmail = email;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
