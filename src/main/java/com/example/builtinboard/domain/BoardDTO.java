package com.example.builtinboard.domain;

import com.example.builtinboard.entity.Board;
import com.example.builtinboard.util.AppUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String inserted;
    @Setter
    private List<BoardFileDTO> files;

    @Builder
    public BoardDTO(Long id, String title, String content, String writer, String inserted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.inserted = inserted;

    }


    public String getAgo(LocalDateTime inserted){
        return AppUtil.getAgo(inserted);
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
