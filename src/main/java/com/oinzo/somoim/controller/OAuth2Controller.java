package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.common.jwt.TokenService;
import com.oinzo.somoim.controller.dto.TokenResponse;
import com.oinzo.somoim.controller.dto.kakaoLoginRequest;
import com.oinzo.somoim.domain.user.service.OAuth2Service;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/oauth")
public class OAuth2Controller {

	private final OAuth2Service oAuth2Service;
	private final JwtProvider jwtProvider;
	private final TokenService tokenService;

	@PostMapping("/kakao")
	public TokenResponse kakaoLogin(
		@RequestBody @Valid kakaoLoginRequest request,
		HttpServletResponse response) {
		Long userId = oAuth2Service.kakaoLogin(request.getCode());
		TokenDto tokenDto = jwtProvider.generateAccessTokenAndRefreshToken(userId);
		String refreshToken = tokenDto.getRefreshToken();
		tokenService.setRefreshTokenCookie(refreshToken, response);
		return TokenResponse.from(tokenDto);
	}

}
