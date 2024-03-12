package com.example.builtinboard.domain;

import com.example.builtinboard.entity.BoardFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFileDTO {
    private Long id;
    private String name;
    private String fileName;

    @Builder
    public BoardFileDTO(Long id, String name, String fileName) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
    }

//    public BoardFile toEntity(){
//
//    }
}
