package com.oinzo.somoim.domain.user.service;

import com.oinzo.somoim.common.type.SocialType;
import com.oinzo.somoim.domain.user.dto.KakaoUserInfoDto;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2Service {

	private final KakaoOAuth2Service kakaoOAuth2Service;
	private final UserRepository userRepository;

	public Long kakaoLogin(String code) {
		String accessToken = kakaoOAuth2Service.getAccessToken(code);
		KakaoUserInfoDto kakaoUserInfo = kakaoOAuth2Service.getUserInfo(accessToken);

		long kakaoId = kakaoUserInfo.getKakaoId();
		User user = userRepository.findBySocialTypeAndSocialId(
			SocialType.KAKAO,
			String.valueOf(kakaoId))
			.orElseGet(() -> userRepository.save(User.from(kakaoUserInfo)));
		return user.getId();
	}
}
