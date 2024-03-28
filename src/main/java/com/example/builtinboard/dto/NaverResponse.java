package com.example.builtinboard.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response{

//    {
//        resultcode=00, message=success, response={id=123123123, name=박재성}
//    }
// 네이버 데이터가 위 JSON 형식으로 응답하므로, Map형식으로 받음
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
