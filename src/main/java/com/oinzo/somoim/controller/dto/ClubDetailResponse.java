package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ClubDetailResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String area;
    private int memberLimit;
    private Long memberCnt;
	private Long likeCnt;
    private Favorite favorite;
    private Long managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

	public static ClubDetailResponse fromClubAndManagerIdAndMemberCntAndLikeCnt(
		Club club, Long managerId, Long memberCnt, Long likeCnt) {
		return ClubDetailResponse.builder()
			.id(club.getId())
			.name(club.getName())
			.description(club.getDescription())
			.imageUrl(club.getImageUrl())
			.area(club.getArea())
			.memberLimit(club.getMemberLimit())
			.memberCnt(memberCnt)
			.likeCnt(likeCnt)
			.favorite(club.getFavorite())
			.managerId(managerId)
			.createdAt(club.getCreatedAt())
			.updatedAt(club.getUpdatedAt())
			.build();
	}
}
