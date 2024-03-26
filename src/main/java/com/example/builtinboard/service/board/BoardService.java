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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    // 기본 파일 저장 위치
    private final String fileBasePath = "src/main/resources/public/boardfiles";
    ;

    // 게시글 생성
    public boolean create(BoardDTO boardDTO, MultipartFile[] files) {
        Board board = boardDTO.toEntity();
        boardRepository.save(board);
        if (files != null && files.length > 0) {
            try {
                saveFiles(board, files);
            } catch (IOException e) {
                log.error("파일 저장 중 오류 : {} " + e.getMessage(), e);
            }
            return true;
        }
        return true;
    }

    private void saveFiles(Board board, MultipartFile[] files) throws IOException{
        // 파일 저장 경로(서비스 할 것이 아니므로, Project 폴더에 저장)
        String uploadPath = System.getProperty("user.dir")
                            + File.separator + "src"
                            + File.separator + "main"
                            + File.separator + "resources"
                            + File.separator + "public"
                            + File.separator + "boardfiles"
                            + File.separator + board.getId();
        File uploadDir = new File(uploadPath);
        // 설정 path가 존재하지 않으면 생성
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        for (MultipartFile file : files) {
            // 파일명 중복 방지
            UUID uuid = UUID.randomUUID();
            String fileName = uuid.toString() + "_" + file.getOriginalFilename();

            BoardFileDTO boardFileDTO = new BoardFileDTO(null, board.getId(), fileName);
            boardFileRepository.save(boardFileDTO.toEntity());
            String filePath = uploadPath + "/" + fileName;
            File fillSave = new File(filePath);
            file.transferTo(fillSave);
        }
    }

    public Page<Board> getBoardList(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (search != null && !search.isEmpty()) {
            // 검색어가 있는 경우
            return boardRepository.findByTitleContainingOrContentContainingOrWriterContaining(search, search, search, pageable);
        } else {
            // 검색어가 없는 경우
            return boardRepository.findAll(pageable);
        }
    }

    public BoardDTO getBoardById(Integer id) {
        // Entity를 DTO로 변환, 조회 데이터가 없는 경우 예외처리
        BoardDTO boardDTO = boardRepository.findById(id)
                .map(Board::toDto)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾지 못했습니다" + id));


        // 게시글에 조회된 파일들을 DTO로 변환
        List<BoardFileDTO> boardFileDTOs = boardFileRepository.findByBoardId(id)
                .stream()
                .map(BoardFile::toDto)
                .collect(Collectors.toList());
        System.out.println("boardFileDTOs.size() = " + boardFileDTOs.size());

        if (!boardFileDTOs.isEmpty()) {
            boardDTO.setFiles(boardFileDTOs);
        }
        System.out.println(boardDTO.getTitle() + boardDTO.getContent() + boardDTO.getWriter() +"files = "+ boardDTO.getFiles());
        return boardDTO;

    }

    public void updateBoardById(Integer id, String title, String content) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾지 못했습니다. id =  " + id));
        board.setTitle(title);
        board.setContent(content);
        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoardById(Integer id) {
        try {
            boardRepository.deleteById(id);
            log.info("Board deleted with id: {}", id);
        } catch (EmptyResultDataAccessException e) {
            log.error("No board found with id: {}", id, e);

            throw new EntityNotFoundException("게시글을 찾지 못했습니다. id = " + id);
        }
    }

    public Resource getFileResources(String boardId, String fileName) throws MalformedURLException {
        Path filePath = Paths.get(fileBasePath + "/" + boardId + "/" + fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if(!resource.exists()){
            throw new RuntimeException("파일을 찾지 못했습니다 = "+ fileName);
        }

        return resource;
    }
}
