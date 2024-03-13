package com.example.builtinboard.entity;

import com.example.builtinboard.domain.BoardDTO;
import com.example.builtinboard.domain.BoardFileDTO;
import com.example.builtinboard.util.AppUtil;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "boardfile")
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Builder
    public BoardFile(Long id, Long boardId, String fileName) {
        this.id = id;
        this.boardId = boardId;
        this.fileName = fileName;
    }
    public BoardFileDTO toDto(){
        return BoardFileDTO.builder()
                .id(id)
                .boardId(boardId)
                .fileName(fileName)
                .build();
    }
}
