package com.example.builtinboard.controller.board;

import com.example.builtinboard.domain.BoardDTO;
import com.example.builtinboard.service.board.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("create")
    public ResponseEntity<HttpStatus> create(@RequestParam String content,
                                             @RequestParam String title,
                                             @RequestParam String writer,
                                             @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files
    ) throws IOException {
        try {
            BoardDTO boardDTO = new BoardDTO(null, content, title, writer, null);
            boardService.create(boardDTO, files);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
