package com.example.builtinboard.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;


/* JWT 생성, 파싱하여 사용자의 인증 정보(사용자ID, 역할, 토큰 만료여부)를 추출 */
@Component
@Slf4j
public class JWTUtil {
    private Key key;

    public JWTUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    // 유저 ID 정보 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    // 유저 권한 정보 추출
    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // 토큰 만료시간 추출
    public Boolean isExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // JWT 토큰 생성
    public String createJwt(String username, String role, Long expiredMillisecond) {
        // Claims: Token payload에 들어가는 데이터
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                // 클레임 등록
                .setClaims(claims)
                // 토큰 발행 시간
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 토큰 만료 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredMillisecond))
                // 시그니처
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 헤더에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request){
        // Authorization key에 담긴 쿠키를 탐색
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        log.info("Cookie 탐색 시작");
        for(Cookie cookie : cookies){
            log.info("추출된 Cookie :{} ", cookie.getName());
            if(cookie.getName().equals("Authorization")){
                authorization = cookie.getValue();
            }
        }
        log.info("추출된 토큰 정보:{}", authorization);

        return authorization;
    }

    // 토큰 정보 검증 메서드
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        // 보안 위반 예외 | JWT 형식 오류
        catch (SecurityException | MalformedJwtException e){
            log.info("유효하지 않은 토큰 ", e);
        }
        // 토큰 만료
        catch (ExpiredJwtException e){
            log.info("만료된 토큰 ", e);
        }
        // 지원되지 않은 토큰
        catch (UnsupportedJwtException e){
            log.info("지원하지 않는 토큰 ", e);
        }
        // 적합하지 않은 인자
        catch (IllegalArgumentException e){
            log.info("JWT 클레임이 비었습니다 ", e);
        }
        return false;
        }
}
