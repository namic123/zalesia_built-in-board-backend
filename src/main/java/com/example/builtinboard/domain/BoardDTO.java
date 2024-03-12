package com.example.builtinboard.domain;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
}
