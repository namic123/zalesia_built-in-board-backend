package com.example.builtinboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "member")
public class Member {
    @Id
    // PK생성 전략: 엔티티의 기본키 필드값이 어떻게 생성될지 정의
    // 아래 전략은 자동 증가 기능(auto_increment)을 이용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "member_id",nullable = false, length = 50)
    private String memberId;

    @Column(name="password", nullable = false, length = 255)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 355)
    private String email;

    @Column(name = "signup_date", nullable = false)
    private LocalDateTime signUpDate;
}
