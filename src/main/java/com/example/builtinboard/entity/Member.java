package com.example.builtinboard.entity;

import com.example.builtinboard.dto.BoardDTO;
import com.example.builtinboard.dto.MemberDTO;
import com.example.builtinboard.util.AppUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    // PK생성 전략: 엔티티의 기본키 필드값이 어떻게 생성될지 정의
    // 아래 전략은 자동 증가 기능(auto_increment)을 이용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "memberId", nullable = false)
    private String memberId;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Setter
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "signup_date", nullable = false)
    private LocalDateTime signupDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Builder
    public Member(Long id, String password, String name, String memberId, String nickname, String email, Role role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
    public void passwordEncode(String password){
        this.password = password;
    }
    @PrePersist
    public void prePersist() {
        this.signupDate = LocalDateTime.now();
    }



    public MemberDTO toDto(){
        return MemberDTO.builder()
                .memberId(memberId)
                .name(name)
                .nickname(nickname)
                .role(role)
                .email(email)
                .build();
    }
}
