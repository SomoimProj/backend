package com.oinzo.somoim.domain.board.repository;

import com.oinzo.somoim.domain.board.entity.ClubBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoard,Long> {

    List<ClubBoard> findAllByClubIdIs(Long clubId);

    Optional<ClubBoard> findById(Long boardId);
}
