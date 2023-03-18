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

	public static TokenResponse from(TokenDto tokenDto) {
		return TokenResponse.builder()
			.accessToken(tokenDto.getAccessToken())
			.accessTokenExpirationDateTime(tokenDto.getAccessTokenExpirationDateTime())
			.refreshToken("HttpOnly")
			.refreshTokenExpirationDateTime(tokenDto.getRefreshTokenExpirationDateTime())
			.build();
	}

}
