package com.jygoh.jyso.domain.member.service;

import com.jygoh.jyso.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

   private final String email;
   private final String password;
   private final String nickname;
   private final Collection<? extends GrantedAuthority> authorities;

   public UserDetailsImpl(String email, String password, String nickname, Collection<? extends GrantedAuthority> authorities) {
       this.email = email;
       this.password = password;
       this.nickname = nickname;
       this.authorities = authorities;
   }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getNickname() {
       return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
