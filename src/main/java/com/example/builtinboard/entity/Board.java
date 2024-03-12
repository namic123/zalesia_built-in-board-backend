package com.example.builtinboard.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.ZoneId;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(name = "title", nullable = false)
    private String title;
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
}
