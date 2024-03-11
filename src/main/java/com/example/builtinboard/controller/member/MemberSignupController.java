package com.example.builtinboard.controller.member;

import com.example.builtinboard.service.member.MemberSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberSignupController {

    private final MemberSignupService memberSignupService;

    // 중복 확인
    @GetMapping(value = "/check", params = "nickname")
    public ResponseEntity<HttpStatus> checkNickname(@RequestParam Optional<String> nickname) {
        System.out.println("controller :  " + nickname);
        // null 여부
        if (nickname.isPresent()) {
            if (memberSignupService.getNickname(nickname.get())) {
                // 409 에러: 리소스 충돌
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                // 200 OK
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } else {
            // null인 경우, 400 에러
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping(value = "/check", params = "email")
    public ResponseEntity<HttpStatus> checkEmail(@RequestParam Optional<String> email){
        if(email.isPresent()){
            if(memberSignupService.getEmail(email.get())){
                // 409 에러: 리소스 충돌
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                // 200 OK
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } else {
            // null인 경우, 400 에러
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping(value = "/check", params = "id")
    public ResponseEntity<HttpStatus> checkId(@RequestParam Optional<String> id){
        if(id.isPresent()){
            if(memberSignupService.getMemberId(id.get())){
                // 409 에러: 리소스 충돌
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                // 200 OK
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } else {
            // null인 경우, 400 에러
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
