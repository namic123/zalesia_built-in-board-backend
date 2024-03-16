package com.example.builtinboard.controller.board;

import com.example.builtinboard.domain.BoardDTO;
import com.example.builtinboard.entity.Board;
import com.example.builtinboard.service.board.BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> view(@PathVariable Integer id){
        log.info("Fetching board with id = ", id);
        BoardDTO boardDTO = boardService.getBoardById(id);
        return ResponseEntity.ok(boardDTO);
    }

    @GetMapping
    public ResponseEntity<?> list(){
        return ResponseEntity.ok(boardService.getBoardList());
    }
    @PostMapping
    public ResponseEntity<HttpStatus> create(@ModelAttribute BoardDTO boardDTO,
                                             @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files
    ) throws IOException {
        log.info("Create a new board with title = ", boardDTO.getTitle());
        boardService.create(boardDTO, files);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
