package com.example.builtinboard.dto;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {


    // 구글 데이터 형식
//    {
//        resultcode=00, message=success, id=123123123, name=재성
//    }
    private final Map<String, Object> attribute;


    public GoogleResponse( Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "google";
    }

    // 구글의 경우 id를 sub로 가져옴
    @Override
    public String getProviderId() {

        return attribute.get("sub").toString();
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
