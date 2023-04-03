package com.oinzo.somoim.domain.clubactivity.repository;

import com.oinzo.somoim.domain.clubactivity.entity.ClubActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubActivityRepository extends JpaRepository<ClubActivity,Long> {

    List<ClubActivity> findAllByClubId(Long clubId);
}
