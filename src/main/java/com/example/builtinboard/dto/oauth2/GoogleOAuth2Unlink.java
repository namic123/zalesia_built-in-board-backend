package com.example.builtinboard.dto.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class GoogleOAuth2Unlink implements OAuth2Unlink{
   // Google OAuth 2.0 연동 해제 API의 URL
    private static final String URL = "https://oauth2.googleapis.com/revoke";
    private final RestTemplate restTemplate;
    @Override
    public void unlink(String accessToken) {

        // MultiValueMap은 key 하나에 여러 값을 매핑할 수 있는 구조
        // 여기서는 POST 요청에 필요한 파라미터를 담음
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);

        // RestTemplate을 사용하여 간단한 HTTP POST 요청을 보내며, 연동 해제 과정을 자동화
        //  연동 해제 요청은 Google OAuth 2.0 API의 정해진 엔드포인트(URL)를 통해 수행
        restTemplate.postForObject(URL, params, String.class);
    }
}
