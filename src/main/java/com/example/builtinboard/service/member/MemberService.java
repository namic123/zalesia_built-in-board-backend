package com.example.builtinboard.service.member;

import com.example.builtinboard.dto.MemberDTO;
import com.example.builtinboard.entity.Member;
import com.example.builtinboard.entity.Role;
import com.example.builtinboard.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean existsByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public boolean existsByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId) != null;
    }

    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
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

}
