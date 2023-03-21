package com.oinzo.somoim.domain.club.repository;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findAllByNameContaining(String name);

    List<Club> findAllByFavoriteAndAreaContaining(Favorite favorite, String area);

    List<Club> findAllByAreaLikeOrderByViewCntDesc(String area);

}
