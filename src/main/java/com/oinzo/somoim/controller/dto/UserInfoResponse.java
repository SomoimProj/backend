package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {

	private String name;
	private String birth;
	private Gender gender;
	private String area;
	private String introduction;
	private String profileUrl;
	private String favorite;

	public static UserInfoResponse from(User user) {
		return UserInfoResponse.builder()
			.name(user.getName())
			.birth(user.getBirth())
			.gender(user.getGender())
			.area(user.getArea())
			.introduction(user.getIntroduction())
			.profileUrl(user.getProfileUrl())
			.favorite(user.getFavorite())
			.build();
	}
}
