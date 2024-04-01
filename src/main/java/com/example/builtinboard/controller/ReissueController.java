package com.example.builtinboard.controller;

import com.example.builtinboard.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
public class ReissueController {
private final JWTUtil jwtUtil;

@PostMapping("/api/reissue")
public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
    log.info("Access 토큰 만료로 Refresh 토큰 재요청 로직 시작", request.getRequestURI());
    String refresh = null;
    refresh = jwtUtil.resolveToken(request).get("authorization");
    if(refresh == null){
        return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
    }

    // 토큰 유효성 검증
    if(!jwtUtil.validateToken(refresh)){
        return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
    }

    // 토큰 카테고리가 refresh인지 확인
    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {
        return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }
    String username = jwtUtil.getUsername(refresh);
    String role = jwtUtil.getRole(refresh);

    // 새 access 토큰 발급
    String updateAccessToken = jwtUtil.createJwt("access", username, role, 1000L);
    String updateRefreshToken = jwtUtil.createJwt("refresh", username, role, 8640000L);

    // 응답
    response.setHeader("Authorization", updateAccessToken);
    response.addCookie(jwtUtil.createCookie("Authorization", updateRefreshToken));
    return ResponseEntity.ok().build();
            }
}
