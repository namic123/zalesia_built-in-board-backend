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

        // Request header에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authorization header 검증(Bearer 다음에 토큰 정보가 포함되므로 공백을 만듬)
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.error("token is null");
            filterChain.doFilter(request, response);
            // token이 비어있는 경우, 메서드 종료
            return;
        }

        logger.info("Authorization 검증 시작");

        // Bearer 제거하고 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        if(jwtUtil.isExpired(token)){
            logger.error("Token 시간 만료");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 아이디와 권한 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // MemberEntity를 생성해서 값 세팅
        Member member = new Member();
        member.setMemberId(username);
        member.setPassword("temppassword");
        member.setRole(Role.valueOf(role));

        // UserDetails에 회원 정보 객체 담기
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // Security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());

        // SecurityContext에 설정함으로써, 사용자가 인증된 상태임을 알림
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 요청 처리 진행
        filterChain.doFilter(request, response);
    }
}
