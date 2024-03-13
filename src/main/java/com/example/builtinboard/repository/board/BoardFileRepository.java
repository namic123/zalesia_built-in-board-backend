package com.example.builtinboard.repository.board;

import com.example.builtinboard.entity.BoardFile;
import com.example.builtinboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Integer> {
    List<BoardFile> findAllByBoardId(Integer boardId);
}
