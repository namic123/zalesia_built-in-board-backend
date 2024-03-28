package com.example.builtinboard.service.auth;

import com.example.builtinboard.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
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

    public void createAuthenticationResponse(HttpServletResponse response, Authentication authentication, String username, String nickname) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 토큰 생성
        String token = jwtUtil.createJwt(username, role, 60 * 60 * 60L);

        // 권한(role) 커스터마이징
        String customRole = role.equals("ADMIN") ? "운영자" : "일반 사용자";

        // 토큰을 헤더에 추가
        response.addHeader("Authorization", "Bearer " + token);


        // 응답 데이터 준비
        Map<String, String> responseData = new HashMap<>();
        responseData.put("memberId", username);
        responseData.put("role", customRole);
        responseData.put("nickname", nickname);

        // JSON 형태로 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), responseData);

    }
}
