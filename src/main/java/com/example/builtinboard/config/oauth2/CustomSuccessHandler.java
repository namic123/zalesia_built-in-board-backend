package com.example.builtinboard.config.oauth2;

import com.example.builtinboard.dto.CustomOAuth2User;
import com.example.builtinboard.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// 인증 성공 후 실행되는 클래스
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;


    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 인증 성공 후, 실행되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());
        String email = customOAuth2User.getEmail();

        // 권한 로드
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(email, role, 60*60*60L);
        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:8080/");
    }

    private Cookie createCookie(String key, String value){
        // Authorization, token이 포함됨
        Cookie cookie = new Cookie(key, value);
        // 60시간 설정
        cookie.setMaxAge(60*60*60);
        // 쿠키 유효 경로, "/"는 모든 경로에서 쿠키가 유효함
        cookie.setPath("/");
        // HttpOnly로 설정, Javascript와 같은 클라이언트 사이드 스크립를 통해 쿠키 접근 불가
        cookie.setHttpOnly(true);
        return cookie;
    }
}
