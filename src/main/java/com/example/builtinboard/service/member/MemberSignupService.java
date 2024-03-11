package com.example.builtinboard.service.member;

import com.example.builtinboard.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberSignupService {
    private final MemberRepository memberRepository;

    public boolean getNickname(String nickname){
        System.out.println("조회 결과 " + memberRepository.findAll());

        return false;
    }

    public boolean getMemberId(String memberId) {
        memberRepository.findByMemberId(memberId);
        return false;
    }

    public boolean getEmail(String email) {
        memberRepository.findByEmail(email);
        return false;
    }
}
