package com.example.builtinboard.dto.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class GithubOAuth2Unlink implements OAuth2Unlink{
    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;
    private final RestTemplate restTemplate;
    @Override
    public void unlink(String accessToken) {
        // GitHub API 엔드포인트 URL 구성
        String url = "https://api.github.com/applications/" + clientId + "/token";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.valueOf("application/vnd.github+json")));
        // 기본 인증 설정
        String base64Credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + base64Credentials);

        // HTTP 바디 설정
        String body = "{\"access_token\":\"" + accessToken + "\"}";

        // HTTP 요청 생성
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // DELETE 요청 실행
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}
