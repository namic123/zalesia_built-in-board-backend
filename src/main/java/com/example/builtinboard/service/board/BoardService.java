package com.example.builtinboard.service.board;

import com.example.builtinboard.domain.BoardDTO;
import com.example.builtinboard.domain.BoardFileDTO;
import com.example.builtinboard.entity.Board;
import com.example.builtinboard.entity.BoardFile;
import com.example.builtinboard.repository.board.BoardFileRepository;
import com.example.builtinboard.repository.board.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // 게시글 생성
    public boolean create(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        Board board = boardDTO.toEntity();

        try {
            boardRepository.save(board);
            if (files != null) {
                // 파일 저장 경로(실제 서비스가 아니므로, Project 폴더에 저장)
                String uploadPath = System.getProperty("user.dir") + "/src/main/java/com/example/builtinboard/files";
                File uploadDir = new File(uploadPath);
                // 앞선 path가 존재하지 않으면 생성
                if(!uploadDir.exists()){
                    uploadDir.mkdir();
                }
                for (MultipartFile file : files) {
                    BoardFileDTO boardFileDTO = new BoardFileDTO(null, board.getId(), file.getOriginalFilename());
                    boardFileRepository.save(boardFileDTO.toEntity());
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Map<String, Object> getBoardList() {
        Map<String, Object> list = new HashMap<>();
        // inserted 형식 변환을 위한 데이터 변환
        List<BoardDTO> boardList = boardRepository.findAll()
                .stream()
                .map(Board::toDto)
                .collect(Collectors.toList());

        list.put("boardList", boardList);
        return list;
    }

    public BoardDTO getBoardById(Integer id) {
        // Entity를 DTO로 변환, 조회 데이터가 없는 경우 예외처리
        BoardDTO boardDTO = boardRepository.findById(id)
                .map(Board::toDto)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾지 못했습니다" + id));

        // 게시글에 조회된 파일들을 DTO로 변환
        List<BoardFileDTO> boardFileDTOs = boardFileRepository.findAllByBoardId(id)
                .stream()
                .map(BoardFile::toDto)
                .collect(Collectors.toList());
        ;

        if (!boardFileDTOs.isEmpty()) {
            boardDTO.setFiles(boardFileDTOs);
        }
        System.out.println(boardDTO.getTitle() + boardDTO.getContent() + boardDTO.getWriter());
        return boardDTO;

    }
}
