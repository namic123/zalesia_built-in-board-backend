package com.example.builtinboard.controller.comment;

import com.example.builtinboard.dto.CommentDto;
import com.example.builtinboard.entity.Comment;
import com.example.builtinboard.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;


    @GetMapping("/{boardId}")
    public ResponseEntity<Page<Comment>> list(@PathVariable Integer boardId,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "5") int size) {
        Page<Comment> comments = commentService.getCommentList(boardId, page, size);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<HttpStatus> create(@PathVariable Integer boardId,
                                             @RequestBody CommentDto commentDto) {
        try {
            log.info("Create a new comment with content = ", commentDto.getContent());
            commentService.create(boardId, commentDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,
                                             @RequestParam("content") String content) {
        log.info("Update comment with id = ", id);
        commentService.updateById(id, content);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id){
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
