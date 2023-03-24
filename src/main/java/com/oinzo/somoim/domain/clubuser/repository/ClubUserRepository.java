package com.oinzo.somoim.domain.clubuser.repository;

import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.clubuser.entity.ClubUser;
import com.oinzo.somoim.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {

	Boolean existsByUserAndClub(User user, Club club);

}
