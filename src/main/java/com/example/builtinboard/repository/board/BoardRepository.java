package com.example.builtinboard.repository.board;

import com.example.builtinboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository <Board,Integer>{
    Page<Board> findAll(Pageable pageable);
}
