package com.example.builtinboard.service.auth;

import com.example.builtinboard.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// 소셜로그인 OAuth2, LoginCustomFilter 인증 성공 후, 공통적으로 jwt를 발급하기 위한 클래스
@Service
public class AuthenticationResponseService {

    private final JWTUtil jwtUtil;

    public AuthenticationResponseService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void createAuthenticationResponse(HttpServletResponse response, Authentication authentication, String username) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*60L);
        System.out.println("생성된 토큰 = " + token);
        response.addCookie(createCookie("Authorization", token));
    }
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
