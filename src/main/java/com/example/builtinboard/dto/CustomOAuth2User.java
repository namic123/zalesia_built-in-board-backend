package com.example.builtinboard.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// OAuth 2.0 로그인 프로세스를 통해 인증된 사용자의 정보를 나타냄.
// 소셜 로그인 Provider로부터 반환된 사용자 속성 정보를 담고 있다.
public class CustomOAuth2User implements OAuth2User {
    private final MemberDTO memberDTO;

    public CustomOAuth2User(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDTO.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return memberDTO.getNickname();
    }
    public String getUsername() {

        return memberDTO.getMemberId();
    }
}
