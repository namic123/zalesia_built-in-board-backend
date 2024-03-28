package com.example.builtinboard.dto;

import com.example.builtinboard.entity.Member;
import com.example.builtinboard.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {
    private Long id;
    private String name;
    private String memberId;
    private String password;
    private String nickname;
    private String email;
    private Role role;

    @Builder
    public MemberDTO(Long id, String name, String memberId, String password, String nickname, String email, Role role) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .name(name)
                .memberId(memberId)
                .password(password)
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
