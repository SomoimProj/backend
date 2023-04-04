package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.jwt.TokenDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenResponse {

	private String accessToken;
	private LocalDateTime accessTokenExpirationDateTime;
	private String refreshToken;
	private LocalDateTime refreshTokenExpirationDateTime;

	public static TokenResponse fromAccessTokenAndRefreshToken(TokenDto accessToken, TokenDto refreshToken) {
		return TokenResponse.builder()
			.accessToken(accessToken.getToken())
			.accessTokenExpirationDateTime(accessToken.getTokenExpirationDateTime())
			.refreshToken("HttpOnly")
			.refreshTokenExpirationDateTime(refreshToken.getTokenExpirationDateTime())
			.build();
	}

	public static TokenResponse fromAccessToken(TokenDto accessToken) {
		return TokenResponse.builder()
			.accessToken(accessToken.getToken())
			.accessTokenExpirationDateTime(accessToken.getTokenExpirationDateTime())
			.build();
	}
}
