package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.clubactivity.entity.ClubActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ClubActivityResponse {
    private Long id;
    private String title;
    private LocalDateTime activityTime;
    private String location;
    private String fee;
    private int memberLimit;
    private int memberCnt;

    public static ClubActivityResponse from(ClubActivity activity){
        return ClubActivityResponse.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .activityTime(activity.getActivityTime())
                .location(activity.getLocation())
                .fee(activity.getFee())
                .memberLimit(activity.getMemberLimit())
                .memberCnt(activity.getMemberCnt())
                .build();
    }

    public static List<ClubActivityResponse> clubActivityResponseToList(List<ClubActivity> activityList){
        return activityList.stream()
                .map(ClubActivityResponse::from)
                .collect(Collectors.toList());
    }
}
