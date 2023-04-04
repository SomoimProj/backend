package com.oinzo.somoim.domain.clubactivity.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.ClubActivityRequest;
import com.oinzo.somoim.controller.dto.ClubActivityResponse;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubactivity.entity.ClubActivity;
import com.oinzo.somoim.domain.clubactivity.repository.ClubActivityRepository;
import com.oinzo.somoim.domain.clubuser.service.ClubUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClubActivityService {
    private final ClubActivityRepository activityRepository;
    private final ClubRepository clubRepository;
    private final ClubUserService clubUserService;

    public ClubActivityResponse addActivity(ClubActivityRequest request, Long userId, Long clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new BaseException(ErrorCode.WRONG_CLUB);
        }
        Long managerId = clubUserService.readClubManagerId(clubId);
        if (!userId.equals(managerId)) {
            throw new BaseException(ErrorCode.NOT_CLUB_MANAGER, "액티비티는 매니저만 생성할 수 있습니다.");
        }
        ClubActivity activity = ClubActivity.from(request, clubId);
        return ClubActivityResponse.from(activityRepository.save(activity));
    }

    public List<ClubActivityResponse> readAllActivity(Long clubId) {
        if (!clubRepository.existsById(clubId))
            throw new BaseException(ErrorCode.WRONG_CLUB);
        List<ClubActivity> activities = activityRepository.findAllByClubId(clubId);
        return ClubActivityResponse.clubActivityResponseToList(activities);
    }

    public ClubActivityResponse updateActivity(ClubActivityRequest request, Long userId, Long activityId) {
        ClubActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_ACTIVITY));
        Long managerId = clubUserService.readClubManagerId(activity.getClubId());
        if (!userId.equals(managerId)) {
            throw new BaseException(ErrorCode.NOT_CLUB_MANAGER, "액티비티는 매니저만 수정할 수 있습니다.");
        }
        activity.updateClubActivity(request);
        return ClubActivityResponse.from(activityRepository.save(activity));
    }

    public void deleteActivity(Long userId, Long activityId) {
        ClubActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new BaseException(ErrorCode.WRONG_ACTIVITY));
        Long managerId = clubUserService.readClubManagerId(activity.getClubId());
        if (!userId.equals(managerId)) {
            throw new BaseException(ErrorCode.NOT_CLUB_MANAGER, "액티비티는 매니저만 삭제할 수 있습니다.");
        }
        activityRepository.delete(activity);
    }
}
