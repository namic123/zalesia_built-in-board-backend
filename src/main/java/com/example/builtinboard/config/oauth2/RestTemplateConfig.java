package com.example.builtinboard.config.oauth2;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// OAuth2 연결 끊기 기능 구현시 HTTP API 요청을 위해 사용.
// RestTemplate은 HTTP 요청을 동기적으로 보내고 받는 데 사용되는 Spring의 중앙 클래스
// 즉, OAuth2 인증을 포함한 외부 시스템과의 HTTP 통신에 유용하게 사용됨.
// 예를들어, OAuth2를 사용하여 외부 시스템에 인증하고 연결을 끊거나 사용자 데이터를 조회하는 드의 API요청을 보낼 때 RestTemplate사용
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }
}
