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

	private String token;
	private Date tokenExpirationIn;

	public LocalDateTime getTokenExpirationDateTime() {
		return convertToLocalDateTime(tokenExpirationIn);
	}

	private LocalDateTime convertToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

}
