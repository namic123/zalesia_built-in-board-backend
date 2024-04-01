package com.example.builtinboard.dto.oauth2;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;

// OAuth2 연동 해제를 위한 관리자
@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2UnlinkManager {
    private final GoogleOAuth2Unlink googleOAuth2Unlink;
    private final NaverOAuth2Unlink naverOAuth2Unlink;
    private final KakaoOAuth2Unlink kakaoOAuth2Unlink;
    private final GithubOAuth2Unlink githubOAuth2Unlink;

    public void unlink(String accessToken){
        if(accessToken.startsWith("google")){
            accessToken = accessToken.substring(6);
            log.info("구글 소셜 로그아웃 시도 :{}", accessToken);
            googleOAuth2Unlink.unlink(accessToken);
        } else if (accessToken.startsWith("naver")) {
            accessToken = accessToken.substring(5);
            log.info("네이버 소셜 로그아웃 시도 :{}", accessToken);
            naverOAuth2Unlink.unlink(accessToken);
        }else if (accessToken.startsWith("kakao")) {
            accessToken = accessToken.substring(5);
            log.info("카카오 소셜 로그아웃 시도 :{}", accessToken);
            kakaoOAuth2Unlink.unlink(accessToken);
        }else if (accessToken.startsWith("github")) {
            accessToken = accessToken.substring(6);
            log.info("깃헙 소셜 로그아웃 시도 :{}", accessToken);
            githubOAuth2Unlink.unlink(accessToken);
        }
        else{
            throw new OAuth2AuthenticationException(
                    "Unlink with " + accessToken.substring(0,6) + "is not supported"
            );
        }

    }
}
