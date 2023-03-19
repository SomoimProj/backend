package com.oinzo.somoim.domain.user.dto;

import com.oinzo.somoim.common.type.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoUserInfoDto {

	private long kakaoId;
	private String profileUrl;
	private Gender gender;
	private String birthday;

}
