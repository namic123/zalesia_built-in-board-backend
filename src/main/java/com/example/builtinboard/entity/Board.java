package com.example.builtinboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="board")
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
    public void prePersist() {
        this.inserted = LocalDateTime.now();
    }
}
