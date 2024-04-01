package com.example.builtinboard.dto.oauth2;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{
    private final Map<String, Object> attributes;
    private final Map<String, Object> attribute;
    private final Map<String, Object> profile;

    public KakaoResponse(Map<String, Object> attributes) {
        // attributes 맵의 kakao_account 키의 값에 실제 attributes 맵이 할당되어 있음
        this.attributes = attributes;
        this.attribute = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) attribute.get("profile");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return getProviderId() + "@kakao.com";
    }

    @Override
    public String getName() {
        return profile.get("nickname").toString();
    }
}