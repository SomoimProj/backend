package com.oinzo.somoim.domain.club.repository;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findAllByNameContaining(String name);

    List<Club> findAllByFavoriteAndAreaContaining(Favorite favorite, String area);

    Page<Club> findAllByAreaLikeOrderByViewCntDesc(String area,Pageable pageable);

    Page<Club> findAllByAreaLikeOrderByCreatedAtDesc(String area,Pageable pageable);

    List<Club> findAllByAreaLike(String area);
}

