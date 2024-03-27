package com.example.builtinboard.domain;

import com.example.builtinboard.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long boardId;
    private String content;
    private String writer;

    @Builder
    public CommentDto(Long id, Long boardId, String content, String writer) {
        this.id = id;
        this.boardId = boardId;
        this.content = content;
        this.writer = writer;
    }

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .writer(writer)
                .build();
    }
}
