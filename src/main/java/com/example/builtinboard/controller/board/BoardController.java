package com.example.builtinboard.controller.board;

import com.example.builtinboard.domain.BoardDTO;
import com.example.builtinboard.entity.Board;
import com.example.builtinboard.service.board.BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Integer id){
        System.out.println("id = " + id);
        try {
            BoardDTO boardDTO = boardService.getBoardById(id);
            System.out.println(boardDTO.getTitle() + boardDTO.getContent() + boardDTO.getWriter());
            return ResponseEntity.ok().body(boardDTO);
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류가 발생했습니다.");
        }
    }

    @GetMapping
    public Map<String, Object> list(){
        return boardService.getBoardList();
    }
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestParam String content,
                                             @RequestParam String title,
                                             @RequestParam String writer,
                                             @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files
    ) throws IOException {
        try {
            BoardDTO boardDTO = new BoardDTO(null, title, content, writer, null);
            boardService.create(boardDTO, files);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
