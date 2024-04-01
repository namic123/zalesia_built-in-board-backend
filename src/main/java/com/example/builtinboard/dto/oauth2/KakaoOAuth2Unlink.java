package com.example.builtinboard.dto.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


@RequiredArgsConstructor
@Component
public class KakaoOAuth2Unlink implements OAuth2Unlink{

    private static final String URL = "https://kapi.kakao.com/v1/user/unlink";
    private final RestTemplate restTemplate;

    @Override
    public void unlink(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Object> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
    }
}
