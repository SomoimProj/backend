package com.oinzo.somoim.domain.clublike.repository;

import com.oinzo.somoim.domain.clublike.entity.ClubLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubLikeRepository extends JpaRepository<ClubLike, Long> {

	boolean existsByClub_IdAndUserId(Long clubId, Long userId);

	Optional<ClubLike> findByClub_IdAndUserId(Long clubId, Long userId);

	List<ClubLike> findAllByUserIdOrderByIdDesc(Long userId);
}
