package com.example.builtinboard.service.board;

import com.example.builtinboard.domain.BoardDTO;
import com.example.builtinboard.domain.BoardFileDTO;
import com.example.builtinboard.entity.Board;
import com.example.builtinboard.repository.board.BoardFileRepository;
import com.example.builtinboard.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // 게시글 생성
    public boolean create(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        System.out.println("Service board = " + boardDTO);
        Board board = boardDTO.toEntity();

        try {
            boardRepository.save(board);
            if (files != null) {
                for (MultipartFile file : files) {
                    BoardFileDTO boardFileDTO = new BoardFileDTO(board.getId(), file.getOriginalFilename());
                    boardFileRepository.save(boardFileDTO.toEntity());
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
