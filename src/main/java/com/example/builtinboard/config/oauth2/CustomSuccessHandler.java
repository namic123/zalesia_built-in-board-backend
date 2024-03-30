package com.example.builtinboard.config.oauth2;

import com.example.builtinboard.dto.CustomOAuth2User;
import com.example.builtinboard.service.auth.AuthenticationResponseService;
import com.example.builtinboard.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증 성공 후 실행되는 클래스
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final AuthenticationResponseService authenticationResponseService;

    public CustomSuccessHandler(JWTUtil jwtUtil, AuthenticationResponseService authenticationResponseService) {
        this.jwtUtil = jwtUtil;
        this.authenticationResponseService = authenticationResponseService;
    }

    // 인증 성공 후, 실행되는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = customOAuth2User.getUsername();

        authenticationResponseService.createAuthenticationResponse(response,authentication,username, customOAuth2User.getAccessToken());
        response.sendRedirect("http://localhost:8080/member/login/validation");
    }

}
