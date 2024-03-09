package com.example.builtinboard.repository.member;

import com.example.builtinboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository {
    Optional<Member> findByNickname(String nickname);
}
