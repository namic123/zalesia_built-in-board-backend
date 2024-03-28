package com.example.builtinboard.service.auth;

import com.example.builtinboard.dto.*;
import com.example.builtinboard.entity.Member;
import com.example.builtinboard.entity.Role;
import com.example.builtinboard.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomOAuth2UserService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 리소스 서버에서 제공되는 유저 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("제공된 유저 정보 :{} ", oAuth2User);

        // 리소스 제공 도메인 구분을 위한 registationId(구글 or 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        log.info("요청 도메인 :{}", registrationId);
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

        Member existData = memberRepository.findByEmail(oAuth2Response.getEmail());
        String memberId = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        String name = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();

        // 존재하지 않는 회원일 경우
        if(existData == null){
            Member member = new Member();
            member.setMemberId(memberId);
            member.passwordEncode( bCryptPasswordEncoder.encode("socialPassword123123!"));
            member.setEmail(email);
            member.setNickname(name);
            member.setRole(Role.GENERAL_MEMBER);

            memberRepository.save(member);
        }
        // 존재하는 회원인 경우
        else{
            existData.setEmail(email);
            existData.setNickname(name);

            memberRepository.save(existData);
        }

        return new CustomOAuth2User(existData.toDto());
    }
}
