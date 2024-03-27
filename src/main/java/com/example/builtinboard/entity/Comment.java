package com.example.builtinboard.entity;

import com.example.builtinboard.domain.CommentDto;
import com.example.builtinboard.util.AppUtil;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name ="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="content", nullable = false)
    private String content;
    @Column(name = "inserted", nullable = false)
    private LocalDateTime inserted;
    @Column(name = "writer", nullable = false)
    private String writer;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, String content, String writer, Board board) {
        this.id = id;
        this.content = content;
        this.writer = writer;
    }

    public String getAgo(){
        return AppUtil.getAgo(inserted);
    }


}
