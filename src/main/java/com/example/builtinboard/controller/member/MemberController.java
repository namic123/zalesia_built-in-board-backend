package com.example.builtinboard.controller.member;

import com.example.builtinboard.dto.MemberDTO;
import com.example.builtinboard.entity.Member;
import com.example.builtinboard.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    // 중복 확인 - 닉네임
    @GetMapping("/check/nickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = memberService.existsByNickname(nickname);
        // 존재 ? 409(리소스 충돌) : 200(OK)
        return exists ? ResponseEntity.status(409).build()
                : ResponseEntity.ok().build();
    }

    // 중복 확인 - 이메일
    @GetMapping("/check/email")
    public ResponseEntity<HttpStatus> checkEmail(@RequestParam("email") String email) {
        boolean exists = memberService.existsByEmail(email);

        return exists ? ResponseEntity.status(409).build()
                : ResponseEntity.ok().build();
    }

    @GetMapping("/check/memberId")
    public ResponseEntity<HttpStatus> checkId(@RequestParam("memberId") String memberId) {
        boolean exists = memberService.existsByMemberId(memberId);

        return exists ? ResponseEntity.status(409).build()
                : ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        boolean created = memberService.createMember(memberDTO);
        return created ? ResponseEntity.status(201).build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/validation")
    public ResponseEntity<Member> getMemberInfo(HttpServletRequest request){
        return ResponseEntity.ok(memberService.getMemberInfo(request));
    }
}
