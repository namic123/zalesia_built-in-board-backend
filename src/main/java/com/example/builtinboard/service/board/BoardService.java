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
    public void create(Board board){
        boardRepository.save(board);

    }
}
