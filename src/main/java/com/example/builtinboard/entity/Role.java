package com.example.builtinboard.entity;

import lombok.Getter;

@Getter // 상수형 자료이므로, Setter 불필요
public enum Role {
    ADMIN("ROLE_ADMIN"),
    GENERAL_MEMBER("ROLE_GENERAL_MEMBER");

    private String value;
    Role(String value){
        this.value = value;

    }}
