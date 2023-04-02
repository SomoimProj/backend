package com.oinzo.somoim.domain.activityuser.repository;

import com.oinzo.somoim.domain.activityuser.entity.ActivityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityUserRepository extends JpaRepository<ActivityUser, Long> {

    Boolean existsByUser_IdAndActivityId(Long userId, Long activityId);

    Optional<ActivityUser> findByUser_IdAndActivityId(Long userId, Long activityId);

    List<ActivityUser> findAllByActivityId(Long activityId);
}
