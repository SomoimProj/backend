package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

	private long userId;
	private String name;
	private String introduction;
	private String profileUrl;

	public static MemberResponse from(User user){
		return MemberResponse.builder()
			.userId(user.getId())
			.name(user.getName())
			.introduction(user.getIntroduction())
			.profileUrl(user.getProfileUrl())
			.build();
	}
}
