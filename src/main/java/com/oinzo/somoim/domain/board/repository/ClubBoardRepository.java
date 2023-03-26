package com.oinzo.somoim.domain.board.repository;

import com.oinzo.somoim.common.type.Category;
import com.oinzo.somoim.domain.board.entity.ClubBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoard,Long> {

    Page<ClubBoard> findAllByClubIdIs(Long clubId, Pageable pageable);

    Page<ClubBoard> findAllByClubIdIsAndCategory(Long clubId, Category category, Pageable pageable);

    Optional<ClubBoard> findById(Long boardId);


}
