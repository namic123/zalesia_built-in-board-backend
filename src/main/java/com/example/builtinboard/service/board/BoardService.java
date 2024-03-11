package com.example.builtinboard.service.board;

import com.example.builtinboard.entity.Board;
import com.example.builtinboard.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 생성
    public boolean create(Board board) {
        if (boardRepository.save(board) != null) {
            return true;
        } else {
            return false;
        }
    }
}
