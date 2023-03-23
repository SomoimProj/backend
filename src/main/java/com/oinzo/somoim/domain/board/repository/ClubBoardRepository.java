package com.oinzo.somoim.domain.board.repository;

import com.oinzo.somoim.domain.board.entity.ClubBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoard,Long> {
}
