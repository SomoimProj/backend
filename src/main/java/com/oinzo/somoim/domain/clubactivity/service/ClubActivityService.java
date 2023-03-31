package com.oinzo.somoim.domain.clubactivity.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.ClubActivityRequest;
import com.oinzo.somoim.controller.dto.ClubActivityResponse;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubactivity.entity.ClubActivity;
import com.oinzo.somoim.domain.clubactivity.repository.ClubActivityRepository;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClubActivityService {
    private final ClubActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ClubUserRepository clubUserRepository;
    private final ClubRepository clubRepository;

    public ClubActivityResponse addActivity(ClubActivityRequest request,Long userId,Long clubId){
        if(!userRepository.existsById(userId))
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        if(!clubRepository.existsById(clubId))
            throw new BaseException(ErrorCode.WRONG_CLUB);
        if(!clubUserRepository.existsByUser_IdAndClub_Id(userId, clubId))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        ClubActivity activity = ClubActivity.from(request,clubId);
        return ClubActivityResponse.from(activityRepository.save(activity));
    }

    public List<ClubActivityResponse> readAllActivity(Long clubId){
        if(!clubRepository.existsById(clubId))
            throw new BaseException(ErrorCode.WRONG_CLUB);
        List<ClubActivity> activities = activityRepository.findAllByClubId(clubId);
        return ClubActivityResponse.clubActivityResponseToList(activities);
    }

    public ClubActivityResponse updateActivity(ClubActivityRequest request, Long userId, Long activityId){
        if(!userRepository.existsById(userId))
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        ClubActivity activity = activityRepository.findById(activityId)
            .orElseThrow(()-> new BaseException(ErrorCode.WRONG_ACTIVITY));
        if(!clubUserRepository.existsByUser_IdAndClub_Id(userId, activity.getClubId()))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        activity.updateClubActivity(request);
        return ClubActivityResponse.from(activityRepository.save(activity));
    }

    public void deleteActivity(Long userId, Long activityId){
        if(!userRepository.existsById(userId))
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        ClubActivity activity = activityRepository.findById(activityId)
                .orElseThrow(()-> new BaseException(ErrorCode.WRONG_ACTIVITY));
        if(!clubUserRepository.existsByUser_IdAndClub_Id(userId, activity.getClubId()))
            throw new BaseException(ErrorCode.NOT_CLUB_MEMBER);
        activityRepository.delete(activity);
    }
}
