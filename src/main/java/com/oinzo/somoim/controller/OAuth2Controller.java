package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.common.jwt.TokenService;
import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.GoogleLoginRequest;
import com.oinzo.somoim.controller.dto.TokenResponse;
import com.oinzo.somoim.controller.dto.KakaoLoginRequest;
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
	public SuccessResponse<TokenResponse> kakaoLogin(
		@RequestBody @Valid KakaoLoginRequest request,
		HttpServletResponse response) {
		Long userId = oAuth2Service.kakaoLogin(request.getCode());

		TokenDto accessToken = jwtProvider.generateAccessToken(userId);
		TokenDto refreshToken = jwtProvider.generateRefreshToken(userId);

		tokenService.setRefreshTokenCookie(refreshToken.getToken(), response);
		TokenResponse tokenResponse = TokenResponse.fromAccessTokenAndRefreshToken(accessToken, refreshToken);
		return ResponseUtil.success(tokenResponse);
	}

	@PostMapping("/google")
	public SuccessResponse<TokenResponse> googleLogin(
		@RequestBody @Valid GoogleLoginRequest request,
		HttpServletResponse response) {
		Long userId = oAuth2Service.googleLogin(request.getCode());

		TokenDto accessToken = jwtProvider.generateAccessToken(userId);
		TokenDto refreshToken = jwtProvider.generateRefreshToken(userId);

		tokenService.setRefreshTokenCookie(refreshToken.getToken(), response);
		TokenResponse tokenResponse = TokenResponse.fromAccessTokenAndRefreshToken(accessToken, refreshToken);
		return ResponseUtil.success(tokenResponse);
	}
}
