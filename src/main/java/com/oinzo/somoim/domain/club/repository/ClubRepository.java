package com.oinzo.somoim.domain.club.repository;

import com.oinzo.somoim.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findAllByNameContaining(String name);

    List<Club> findAllByFavoriteContainingAndAreaContaining(String favorite,String area);

    List<Club> findAllByAreaLikeOrderByCntDesc(String area);


}