package com.example.builtinboard.domain;

import com.example.builtinboard.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private List<BoardFileDTO> files;

    @Builder
    public BoardDTO(Long id, String title, String content, String writer, List<BoardFileDTO> files) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.files = files;
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
