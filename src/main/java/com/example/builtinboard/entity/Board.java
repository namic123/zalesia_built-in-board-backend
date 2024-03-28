package com.example.builtinboard.entity;

import com.example.builtinboard.dto.BoardDTO;
import com.example.builtinboard.util.AppUtil;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.ZoneId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Setter
    @Column(name = "title", nullable = false)
    private String title;
    @Setter
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "inserted", nullable = false)
    private LocalDateTime inserted;
    @Column(name = "writer", nullable = false)
    private String writer;

    @PrePersist
    public void setKoreaTimeZone() {
        this.inserted = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @Builder
    public Board(Long id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
    public String getAgo(){
        return AppUtil.getAgo(inserted);
    }
    public BoardDTO toDto(){
        return BoardDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .inserted(AppUtil.getAgo(inserted))
                .build();
    }
}
