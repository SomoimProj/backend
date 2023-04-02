package com.oinzo.somoim.domain.boardcomment.repository;

import com.oinzo.somoim.domain.boardcomment.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment,Long> {

    List<BoardComment> findAllByBoard_Id(Long boardId);

    int countAllByBoard_Id(Long boardId);
}
