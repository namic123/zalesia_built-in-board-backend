package com.example.builtinboard.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


/* JWT 생성, 파싱하여 사용자의 인증 정보(사용자ID, 역할, 토큰 만료여부)를 추출 */
@Component
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
                .get("memberId", String.class);
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
    public String createJwt(String memberId, String role, Long expiredMillisecond) {
        // Claims: Token payload에 들어가는 데이터
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
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
}
