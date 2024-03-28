package com.example.builtinboard.config.security;

import com.example.builtinboard.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// AuthenticationManger가 검증할 수 있는 사용자의 인증 정보를 담는 컴테이너 역할을 함.
public class CustomMemberDetails implements UserDetails {
    private final Member member;

    public CustomMemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // GrantedAuthority는 Security에서 권한 부여에 사용되는 핵심 인터페이스
        // 사용자가 요청한 작업을 수행할 수 있는 권한을 가지고 있는지 검증해줌.
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
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

    public String getNickname() {
        return member.getNickname();
    }
}
