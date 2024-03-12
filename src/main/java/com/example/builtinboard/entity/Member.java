package com.example.builtinboard.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "memberId", nullable = false)
    private String memberId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "signup_date", nullable = false)
    private LocalDateTime signupDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Builder
    public Member(Long id, String name, String memberId, String password, String nickname, String email, Role role) {
        this.name = name;
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public void passwordEncode(String password){
        this.password = password;
    }
    @PrePersist
    public void prePersist() {
        this.signupDate = LocalDateTime.now();
    }
}
