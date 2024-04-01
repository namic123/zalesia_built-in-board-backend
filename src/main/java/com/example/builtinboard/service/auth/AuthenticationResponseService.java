package com.example.builtinboard.service.auth;

import com.example.builtinboard.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    public void createAuthenticationResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String username, String OAuthAccessToken, String nickname) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwt("access", username, role, 1L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        System.out.println("생성된 Access토큰 = " + accessToken);
        System.out.println("생성된 Refresh토큰 = " + refreshToken);
        response.setHeader("authorization", accessToken);
        response.addCookie(jwtUtil.createCookie("Authorization", refreshToken));
        if(OAuthAccessToken != null){
            response.addCookie(jwtUtil.createCookie("_OAuth2", OAuthAccessToken));
        }

        if(request.getRequestURI().equals("/api/members/login")){
            Map<String, String> data = new HashMap<>();
            data.put("memberId", username);
            data.put("nickname", nickname);
            String jsonData = new ObjectMapper().writeValueAsString(data);
            // 응답 헤더 설정
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 응답 본문에 JSON 문자열 작성
            response.getWriter().write(jsonData);
            response.getWriter().flush();
        }
    }

}
