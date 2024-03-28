package com.example.builtinboard.dto;

import com.example.builtinboard.entity.BoardFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFileDTO {
    private Long id;
    private Long boardId;
    private String fileName;

    @Builder
    public BoardFileDTO(Long id, Long boardId, String fileName) {
        this.id = id;
        this.boardId = boardId;
        this.fileName = fileName;
    }

    public BoardFile toEntity() {
        return BoardFile.builder()
                .id(id)
                .boardId(boardId)
                .fileName(fileName)
                .build();
    }
}
