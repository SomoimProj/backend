package com.oinzo.somoim.domain.clubuser.repository;

import com.oinzo.somoim.domain.clubuser.entity.ClubUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {

	Boolean existsByUser_IdAndClub_Id(Long userId, Long clubId);

	List<ClubUser> findByClub_Id(Long clubId);
	List<ClubUser> findByUser_Id(Long userId);

}
