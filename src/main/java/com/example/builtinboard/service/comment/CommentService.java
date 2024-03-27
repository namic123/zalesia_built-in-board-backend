package com.example.builtinboard.service.comment;

import com.example.builtinboard.domain.CommentDto;
import com.example.builtinboard.entity.Board;
import com.example.builtinboard.entity.Comment;
import com.example.builtinboard.repository.board.BoardRepository;
import com.example.builtinboard.repository.comment.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void create(Integer boardId, CommentDto commentDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Board id: " + boardId));


        Comment comment = commentDto.toEntity();
        comment.setBoard(board);
        comment.setInserted(LocalDateTime.now());

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getCommentList(Integer boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Comment> commentList = commentRepository.findAllByBoardId(boardId, pageable);

        return commentList;
    }

    @Transactional
    public void updateById(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾지 못했습니다 = " + id));
        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
