package com.example.builtinboard.dto.oauth2;

import java.util.Map;

public class GithubResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public GithubResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("login").toString() + "@github.com";
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
