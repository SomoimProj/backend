package com.oinzo.somoim.domain.activityuser.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.ActivityUserResponse;
import com.oinzo.somoim.domain.activityuser.entity.ActivityUser;
import com.oinzo.somoim.domain.activityuser.repository.ActivityUserRepository;
import com.oinzo.somoim.domain.clubactivity.entity.ClubActivity;
import com.oinzo.somoim.domain.clubactivity.repository.ClubActivityRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ActivityUserService {
    private final ActivityUserRepository activityUserRepository;
    private final ClubActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ClubUserRepository clubUserRepository;

    public ActivityUserResponse addActivityUser(Long userId, Long activityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        ClubActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_ACTIVITY));
        if (activityUserRepository.existsByUser_IdAndActivityId(userId, activityId)) {
            throw new BaseException(ErrorCode.ALREADY_JOINED_ACTIVITY);
        }
        if (!clubUserRepository.existsByUser_IdAndClub_Id(userId, activity.getClubId())) {
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        }
        if (activity.getMemberLimit() == activity.getMemberCnt()) {
            throw new BaseException(ErrorCode.ACTIVITY_LIMIT_OVER);
        }
        activity.plusMemberCnt();
        ActivityUser activityUser = ActivityUser.builder()
                .user(user)
                .activityId(activity.getId())
                .build();
        return ActivityUserResponse.from(activityUserRepository.save(activityUser));
    }

    public List<ActivityUserResponse> readAllActivityUser(Long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new BaseException(ErrorCode.WRONG_ACTIVITY);
        }
        List<ActivityUser> users = activityUserRepository.findAllByActivityId(activityId);
        return ActivityUserResponse.activityUserResponsesList(users);
    }

    public void deleteActivityUser(Long userId, Long activityId) {
        ClubActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_ACTIVITY));
        ActivityUser activityUser = activityUserRepository.findByUser_IdAndActivityId(userId, activityId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_ACTIVITY_USER));
        activity.minusMemberCnt();
        activityUserRepository.delete(activityUser);
    }
}
