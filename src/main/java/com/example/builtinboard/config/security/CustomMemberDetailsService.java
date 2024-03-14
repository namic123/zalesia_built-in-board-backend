package com.example.builtinboard.config.security;

import com.example.builtinboard.config.security.CustomMemberDetails;
import com.example.builtinboard.entity.Member;
import com.example.builtinboard.repository.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 사용자 ID와 Password를 받아 AuthenticationManger가 검증 가능한 UserDetails 객체에 담아 반환하는 역할을 함.
@Service
public class CustomMemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomMemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username);
        if(member != null){
            // AuthenticationManger가 검증할 수 있도록 UserDetails에 담음
            return new CustomMemberDetails(member);
        }

        return null;
    }
}
