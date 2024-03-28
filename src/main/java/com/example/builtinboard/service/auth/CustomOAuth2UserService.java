package com.example.builtinboard.service.auth;

import com.example.builtinboard.dto.*;
import com.example.builtinboard.entity.Role;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 리소스 서버에서 제공되는 유저 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        // 리소스 제공 도메인 구분을 위한 registationId(구글 or 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 리소스를 받을 OAuth2 DTO 인터페이스 초기화
        OAuth2Response oAuth2Response = null;

        // 리소스 제공 도메인에 따라 데이터 할당
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(oAuth2Response.getEmail());
        memberDTO.setNickname(oAuth2Response.getName());
        memberDTO.setRole(Role.GENERAL_MEMBER);
        return new CustomOAuth2User(memberDTO);
    }
}
