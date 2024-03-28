package com.example.builtinboard.controller.board;

import com.example.builtinboard.dto.BoardDTO;
import com.example.builtinboard.entity.Board;
import com.example.builtinboard.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> view(@PathVariable Integer id) {
        log.info("Fetching board with id = ", id);
        BoardDTO boardDTO = boardService.getBoardById(id);
        return ResponseEntity.ok(boardDTO);
    }

    @GetMapping
    public ResponseEntity<Page<Board>> list(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search
    ) {
        return ResponseEntity.ok(boardService.getBoardList(page, size, search));
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

    // 파일 다운로드
    @GetMapping("/download/{boardId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String boardId,
                                                 @PathVariable String fileName) throws MalformedURLException, UnsupportedEncodingException {
        System.out.println("fileName = " + fileName);
        Resource resource = boardService.getFileResources(boardId, fileName);

        // 파일명 UTF-8로 인코딩하여 브라우저에서 이해할 수 있는 형태로 변환
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace("+","%20");

        String contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}