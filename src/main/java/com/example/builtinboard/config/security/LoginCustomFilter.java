package com.example.builtinboard.config.security;

import com.example.builtinboard.service.auth.AuthenticationResponseService;
import com.example.builtinboard.util.JWTUtil;
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
// 이 필터에 의해 로그인 검증이되므로, 따로 Controller를 작성할 필요가 없음.
public class LoginCustomFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final AuthenticationResponseService authenticationResponseService;


    public LoginCustomFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,AuthenticationResponseService authenticationResponseService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authenticationResponseService = authenticationResponseService;
        // 커스텀 로그인 경로 설정
        setFilterProcessesUrl("/api/members/login");
    }

    // 요청에서 아이디와 비밀번호 추출 후, 토큰에 담아서 authenticationManager에 전달하는 역할을함.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 요청에서 id와 password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        // username과 password 검증을 위해 token에 담는다
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // authenticationManager로 전달
        return authenticationManager.authenticate(authenticationToken);
    }

    // 로그인 검증 성공 후, 실행 메서드(JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // getPrincipal 메서드를 통해 현재 인증된 사용자의 세부 정보 추출
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authResult.getPrincipal();

        String username = customMemberDetails.getUsername();
        System.out.println("username = " + username);
        // 권한 정보 추출 후 JWT 발급을 위한 클래스
        authenticationResponseService.createAuthenticationResponse(response, authResult, username);

    }

    // 로그인 검증 실패시, 실행 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 인증 실패시 401(Unauthorized) 응답
        response.setStatus(401);
    }
}
