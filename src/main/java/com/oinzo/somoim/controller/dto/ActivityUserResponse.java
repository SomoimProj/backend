package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.activityuser.entity.ActivityUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ActivityUserResponse {

    private Long userId;
    private String userName;
    private String userImage;
    private Long activityId;

    public static ActivityUserResponse from(ActivityUser activityUser) {
        return ActivityUserResponse.builder()
                .userId(activityUser.getUser().getId())
                .userName(activityUser.getUser().getName())
                .userImage(activityUser.getUser().getProfileUrl())
                .activityId(activityUser.getActivityId())
                .build();
    }

    public static List<ActivityUserResponse> activityUserResponsesList(List<ActivityUser> users) {
        return users
                .stream()
                .map(ActivityUserResponse::from)
                .collect(Collectors.toList());
    }

}
