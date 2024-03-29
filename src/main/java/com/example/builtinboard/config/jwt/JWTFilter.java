package com.example.builtinboard.config.jwt;

import com.example.builtinboard.config.security.CustomMemberDetails;
import com.example.builtinboard.entity.Member;
import com.example.builtinboard.entity.Role;
import com.example.builtinboard.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// HTTP 요청에 대해 한 번씩 실행되며, JWT 토큰 검증 및 유효성 확인 후, SecurityContext에 인증 정보를 설정
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getRequestURI());
        // Request header에서 Authorization 헤더를 찾고, token 반환
        String token = jwtUtil.resolveToken(request);
        if(token != null){
             logger.info("Authorization 검증 시작");
                 if (jwtUtil.validateToken(token)) {
                     // 토큰에서 아이디와 권한 획득
                     String username = jwtUtil.getUsername(token);
                     String role = jwtUtil.getRole(token);

                     // MemberEntity를 생성해서 값 세팅
                     Member member = new Member();
                     member.setMemberId(username);
                     member.setRole(Role.valueOf(role));

                     // UserDetails에 회원 정보 객체 담기
                     CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

                     // Security 인증 토큰 생성
                     Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());

                     // SecurityContext에 설정함으로써, 사용자가 인증된 상태임을 알림
                     SecurityContextHolder.getContext().setAuthentication(authToken);


                 }else{
                     jwtUtil.resolveExpiredJwtCookie(request,response);
                     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                     return;
                 }

        }
        // 요청 처리 진행
        filterChain.doFilter(request, response);
    }
}
