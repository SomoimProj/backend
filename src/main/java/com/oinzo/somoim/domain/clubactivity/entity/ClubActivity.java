package com.oinzo.somoim.domain.clubactivity.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.controller.dto.ClubActivityRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ClubActivity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long clubId;
    @NotBlank
    private String title;
    @NotNull
    private LocalDateTime activityTime;
    private String location;
    @NotBlank
    private String fee;
    @NotNull
    private int memberLimit;
    @NotNull
    private int memberCnt;

    public void plusMemberCnt() {
        memberCnt++;
    }

    public void minusMemberCnt() {
        memberCnt--;
    }

    public static ClubActivity from(ClubActivityRequest request, Long clubId) {
        return ClubActivity.builder()
                .clubId(clubId)
                .title(request.getTitle())
                .activityTime(request.getActivityTime())
                .location(request.getLocation())
                .fee(request.getFee())
                .memberLimit(request.getMemberLimit())
                .memberCnt(0)
                .build();
    }

    public void updateClubActivity(ClubActivityRequest activityRequest) {
        if (activityRequest.getTitle() != null)
            this.title = activityRequest.getTitle();
        if (activityRequest.getActivityTime() != null)
            this.activityTime = activityRequest.getActivityTime();
        if (activityRequest.getLocation() != null)
            this.location = activityRequest.getLocation();
        if (activityRequest.getFee() != null) this.fee = activityRequest.getFee();
        if (activityRequest.getMemberLimit() != 0)
            this.memberLimit = activityRequest.getMemberLimit();
    }
}
