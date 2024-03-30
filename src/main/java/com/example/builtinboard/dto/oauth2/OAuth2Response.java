package com.example.builtinboard.dto.oauth2;

// 소셜 Response에 대한 DTO의 일관성을 위한 인터페이스
public interface OAuth2Response {
    //리소스 제공자 (Ex. naver, google, ...)
    String getProvider();
    //리소스 제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    // 이메일
    String getEmail();
    // 사용자 실명 (설정한 이름)
    String getName();

}
