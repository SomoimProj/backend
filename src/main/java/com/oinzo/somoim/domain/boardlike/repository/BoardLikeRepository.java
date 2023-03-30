package com.oinzo.somoim.domain.boardlike.repository;

import com.oinzo.somoim.domain.boardlike.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {

    boolean existsByBoard_IdAndUser_Id(Long boardId,Long userId);

    Optional<BoardLike> findByBoard_IdAndUser_Id(Long boardId, Long userId);

    List<BoardLike> findAllByBoard_Id(Long boardId);
}
