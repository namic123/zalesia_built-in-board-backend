package com.example.builtinboard.domain;

import com.example.builtinboard.entity.Board;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;

    @Builder
    public BoardDTO(Long id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Board toEntity(){
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }
}
