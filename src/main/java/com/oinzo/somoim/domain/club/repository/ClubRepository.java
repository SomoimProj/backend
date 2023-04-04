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

    boolean existsById(Long id);

    List<Club> findAllByNameContaining(String name);

    List<Club> findAllByFavoriteAndAreaContaining(Favorite favorite, String area);

    Page<Club> findAllByAreaLikeOrderByViewCntDescIdDesc(String area,Pageable pageable);

    Page<Club> findAllByAreaLikeOrderByCreatedAtDescIdDesc(String area,Pageable pageable);

    List<Club> findAllByNameContainingAndFavorite(String name, Favorite favorite);

}

