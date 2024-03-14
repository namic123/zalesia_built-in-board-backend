package com.example.builtinboard.config.security;

import com.example.builtinboard.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// 로그인 검증을 위한 커스텀 필터
// 이 필터에 의해 로그인 검증이되므로, 따로 Controller를 작성할 필요가 없음.
public class LoginCustomFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginCustomFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        // 커스텀 로그인 경로 설정
        setFilterProcessesUrl("/api/member/login");
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
        // getPrincipal 메서드를 통해 현재 인증된 사용자의 세부 정보 추출
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authResult.getPrincipal();
        // 인증된 id 및 권한 정보 추출
        String memberId = customMemberDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        // 토큰 생성
        String token = jwtUtil.createJwt(memberId, role, 60 * 60 * 10L);
        // 응답 헤더에 Authoriztion 헤더를 추가 (Bearer 형식, token을 헤더에 담음)
        response.addHeader("Authorization", "Bearer " + token);
    }

    // 로그인 검증 실패시, 실행 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 인증 실패시 401(Unauthorized) 응답
        response.setStatus(401);
    }
}
