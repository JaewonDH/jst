package com.jst.domain.member.service;

import com.jst.domain.member.repository.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        //계정의 만료 되지 않았는지 확인 true 만료 되지 않음을 의미
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겨 있지 않지를 리턴 true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //계정의 패스워드가 만료 되지 않았는지 확인
        return true;
    }

    @Override
    public boolean isEnabled() {
        //계정이 사용 가능한 계정인지 확인 true가 정상
        return true;
    }
}
