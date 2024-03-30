package com.example.builtinboard.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@Component
public class NaverOAuth2Unlink implements OAuth2Unlink{
    // Naver의 Unlink URL
    private static final String URL = "https://nid.naver.com/oauth2.0/token?";
    //  OAuth2를 사용하여 외부 시스템에 인증하고 연결을 끊거나 사용자 데이터를 조회하는 드의 API요청을 보낼 때 RestTemplate 사용
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    // 연동 해제 메서드
    @Override
    public void unlink(String accessToken) {
        String url = URL +
                     "&grant_type=delete" +
                     "&client_id=" +
                     clientId +
                     "&client_secret=" +
                     clientSecret +
                     "&access_token=" +
                     accessToken +
                     "&service_provider=NAVER";

        log.info("연동해제 요청 URL :{}", url);
        UnlinkResponse response = restTemplate.getForObject(url, UnlinkResponse.class);

      // 요청의 응답으로 UnlinkResponse 객체를 받아, 연동 해제의 성공 여부를 판단
        if(response != null && !"success".equalsIgnoreCase(response.getResult())){
            log.error("Naver 연동해제 실패 ");
         throw new RuntimeException("Failed to Naver Unlink");
        }
    }

    // 네이버로부터 받은 연동 해제 응답을 매핑하기 위한 내부 정적 클래스
    // @JsonProperty("access_token"): JSON 응답에서 access_token 필드를 accessToken 변수에 매핑
    // private final String accessToken과
    // private final String result: 응답에서 받은 액세스 토큰 값과 연동 해제 요청의 결과를 저장.
    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse{
        @JsonProperty("access_token")
        private final String accessToken;
        private final String result;
    }
}
