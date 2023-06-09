package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {

	private long id;
	private String name;
	private LocalDate birth;
	private Gender gender;
	private String area;
	private String introduction;
	private String profileUrl;
	private List<Favorite> favorites;

	public static UserInfoResponse from(User user) {
		return UserInfoResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.birth(user.getBirth())
			.gender(user.getGender())
			.area(user.getArea())
			.introduction(user.getIntroduction())
			.profileUrl(user.getProfileUrl())
			.favorites(user.getFavorites())
			.build();
	}
}
