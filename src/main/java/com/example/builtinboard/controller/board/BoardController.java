package com.example.builtinboard.controller.board;

import com.example.builtinboard.domain.BoardDTO;
import com.example.builtinboard.entity.Board;
import com.example.builtinboard.service.board.BoardService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    // 기본 파일 저장 위치
    private final String fileBasePath = "src/main/resources/public/boardfiles";
    ;

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> view(@PathVariable Integer id) {
        log.info("Fetching board with id = ", id);
        BoardDTO boardDTO = boardService.getBoardById(id);
        return ResponseEntity.ok(boardDTO);
    }

    @GetMapping
    public ResponseEntity<Page<Board>> list(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(boardService.getBoardList(page, size));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestParam("writer") String writer,
                                             @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files
    ) throws IOException {
        BoardDTO boardDTO = new BoardDTO(null, title, content, writer, null);
        log.info("Create a new board with title = ", boardDTO.getTitle());
        boardService.create(boardDTO, files);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Integer id,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content
    ) {
        log.info("Update board with id = ", id);
        boardService.updateBoardById(id, title, content);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
        boardService.deleteBoardById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/download/{boardId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String boardId,
                                                 @PathVariable String fileName) {
        Resource resource = boardService.getFileResources(boardId, fileName);
    }
}