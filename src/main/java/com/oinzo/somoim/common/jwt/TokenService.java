package com.oinzo.somoim.common.jwt;

import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

	public void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
			.maxAge(JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME / 1000)
			.path("/")
//			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();
		response.addHeader("Set-Cookie", refreshTokenCookie.toString());
	}
}
