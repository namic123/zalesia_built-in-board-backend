package com.example.builtinboard.controller.board;

import com.example.builtinboard.entity.Board;
import com.example.builtinboard.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;
    @PostMapping("create")
    public ResponseEntity create(Board board){
            boardService.create(board);
            return ResponseEntity.ok().build();
    }
}
