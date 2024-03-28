package com.example.builtinboard.repository.member;

import com.example.builtinboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByNickname(String nickname);
    Member findByEmail(String email);
    Member findByMemberId(String memberId);
}
