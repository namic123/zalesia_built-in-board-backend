package com.example.builtinboard.service.member;

import com.example.builtinboard.dto.MemberDTO;
import com.example.builtinboard.dto.oauth2.OAuth2UnlinkManager;
import com.example.builtinboard.entity.Member;
import com.example.builtinboard.entity.Role;
import com.example.builtinboard.repository.member.MemberRepository;
import com.example.builtinboard.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final OAuth2UnlinkManager oAuth2UnlinkManager;


    public boolean existsByNickname(String nickname) {
        return memberRepository.findByNickname(nickname) != null;
    }

    public boolean existsByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId) != null;
    }

    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email) != null;
    }

    public boolean createMember(MemberDTO memberDTO) {
        try {
            Member member = memberDTO.toEntity();
            String password = bCryptPasswordEncoder.encode(memberDTO.getPassword());
            member.passwordEncode(password);
            member.setRole(Role.GENERAL_MEMBER);
            memberRepository.save(member);
            return true;
        }catch (DataAccessException e){
            log.error("멤버 생성 과정에 문제 발생 : {} ",e);
            return false;
        }
    }

    public Map<String,Object> getMemberInfo(HttpServletRequest request) {
        Map<String, String> auth = jwtUtil.resolveToken(request);
        Map<String, Object> memberInfo = new HashMap<>();

        String token = auth.get("authorization");

        if(token != null){
            String username = jwtUtil.getUsername(token);
            MemberDTO member = memberRepository.findByMemberId(username).toDto();
            memberInfo.put("member", member);
            return memberInfo;
        }    
        return null;
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> tokens = jwtUtil.resolveToken(request);
        log.info("로그아웃 토큰, Contains Authorization? :{}", tokens.get("authorization"));
        log.info("로그아웃 토큰, Contains _OAuth2? :{}", tokens.get("oauth2AccessToken"));
        if(tokens.containsKey("oauth2AccessToken")){
            String token = tokens.get("oauth2AccessToken");
            oAuth2UnlinkManager.unlink(token);
        }
        return jwtUtil.resolveExpiredJwtCookie(request,response);
    }
}
