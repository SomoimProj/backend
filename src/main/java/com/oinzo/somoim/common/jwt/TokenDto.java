package com.oinzo.somoim.common.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {

	private String accessToken;
	private Date accessTokenExpirationIn;
	private String refreshToken;
	private Date refreshTokenExpirationIn;

	public LocalDateTime getAccessTokenExpirationDateTime() {
		return convertToLocalDateTime(accessTokenExpirationIn);
	}

	public LocalDateTime getRefreshTokenExpirationDateTime() {
		return convertToLocalDateTime(refreshTokenExpirationIn);
	}

	private LocalDateTime convertToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

}
