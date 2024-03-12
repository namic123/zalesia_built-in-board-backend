package com.example.builtinboard.service.member;

import com.example.builtinboard.domain.MemberDTO;
import com.example.builtinboard.entity.Member;
import com.example.builtinboard.entity.Role;
import com.example.builtinboard.repository.member.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberSignupService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean getNickname(String nickname) {
        // 닉네임이 존재할 경우
        if (memberRepository.findByNickname(nickname).isPresent()) {

            return true;
        }
        // 존재하지 않을 경우
        return false;
    }

    public boolean getMemberId(String memberId) {
        // 아이디가 존재할 경우
        if (memberRepository.findByMemberId(memberId).isPresent()) {
            return true;
        }
        // 존재하지 않을 경우
        return false;
    }

    public boolean getEmail(String email) {
        // 이메일이 존재할 경우
        if (memberRepository.findByEmail(email).isPresent()) {
            return true;
        }
        // 존재하지 않는 경우
        return false;
    }

    public boolean createMember(@Valid MemberDTO memberDTO) {
        try {
            Member member = memberDTO.toEntity();
            String password = bCryptPasswordEncoder.encode(memberDTO.getPassword());
            member.passwordEncode(password);
            member.setRole(Role.GENERAL_MEMBER);
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
