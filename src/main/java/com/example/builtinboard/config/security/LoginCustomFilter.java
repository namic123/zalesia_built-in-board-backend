package com.example.builtinboard.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

// 로그인 검증을 위한 커스텀 필터
public class LoginCustomFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public LoginCustomFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;


    }

    // 요청에서 아이디와 비밀번호 추출 후, 토큰에 담아서 authenticationManager에 전달하는 역할을함.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 요청에서 id와 password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // username과 password 검증을 위해 token에 담는다
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // authenticationManager로 전달
        return authenticationManager.authenticate(authenticationToken);
    }

    // 로그인 검증 성공 후, 실행 메서드(JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
    // 로그인 검증 실패시, 실행 메서드

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
