package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
	private int memberCnt;
	private Favorite favorite;
	private Long managerId;

	public static ClubDetailResponse fromClubAndManagerId(Club club, Long managerId){
		return ClubDetailResponse.builder()
			.id(club.getId())
			.name(club.getName())
			.description(club.getDescription())
			.imageUrl(club.getImageUrl())
			.area(club.getArea())
			.memberLimit(club.getMemberLimit())
			.memberCnt(club.getMemberCnt())
			.favorite(club.getFavorite())
			.managerId(managerId)
			.build();
	}

}
