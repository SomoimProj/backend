package com.oinzo.somoim.domain.user.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.common.type.SocialType;
import com.oinzo.somoim.domain.user.dto.GoogleUserInfoDto;
import com.oinzo.somoim.domain.user.dto.KakaoUserInfoDto;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String birth;
	@Enumerated(value = EnumType.STRING)
	private Gender gender;
	private String area;
	private String introduction;
	private String profileUrl;
	@Enumerated(value = EnumType.STRING)
	private Favorite favorite;

	private String email;
	private String password;

	@Enumerated(value = EnumType.STRING)
	private SocialType socialType;
	private String socialId;

	public static User from(KakaoUserInfoDto kakaoUserInfoDto) {
		return User.builder()
			.socialType(SocialType.KAKAO)
			.socialId(String.valueOf(kakaoUserInfoDto.getKakaoId()))
//            .birth(kakaoUserInfoDto.getBirthday())
			.gender(kakaoUserInfoDto.getGender())
			.profileUrl(kakaoUserInfoDto.getProfileUrl())
			.build();
	}

	public static User from(GoogleUserInfoDto googleUserInfoDto) {
		return User.builder()
			.socialType(SocialType.GOOGLE)
			.socialId(googleUserInfoDto.getGoogleId())
			.name(googleUserInfoDto.getName())
			.profileUrl(googleUserInfoDto.getProfileUrl())
			.build();
	}
}
