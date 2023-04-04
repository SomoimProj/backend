package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.common.jwt.TokenService;
import com.oinzo.somoim.common.redis.RedisService;
import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.*;
import com.oinzo.somoim.domain.user.service.AuthService;
import com.oinzo.somoim.domain.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/users/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final EmailService emailService;
	private final AuthService authService;
	private final JwtProvider jwtProvider;
	private final TokenService tokenService;
	private final RedisService redisService;

	@PostMapping("/email/send")
	public SuccessResponse<VerificationCodeResponse> sendMail(@RequestBody @Valid EmailRequest emailRequest) {
		String code = emailService.sendVerificationCode(emailRequest.getEmail());
		VerificationCodeResponse verificationCodeResponse = new VerificationCodeResponse(code);
		return ResponseUtil.success(verificationCodeResponse);
	}

	@PostMapping("/email/check")
	public SuccessResponse<CheckResponse> checkCode(@RequestBody @Valid EmailRequest emailRequest) {
		boolean result = emailService.checkVerificationCode(emailRequest.getEmail(), emailRequest.getCode());
		CheckResponse checkResponse = new CheckResponse(result);
		return ResponseUtil.success(checkResponse);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/signup")
	public SuccessResponse<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		authService.signUp(signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getPasswordCheck());

		String emailInRedis = "email:" + signUpRequest.getEmail();
		if (redisService.get(emailInRedis) != null) {
			redisService.delete(emailInRedis);
		}
		return ResponseUtil.success();
	}

	@PostMapping("/signin")
	public SuccessResponse<TokenResponse> signIn(@RequestBody @Valid SignInRequest signInRequest,
												 HttpServletResponse response) {
		Long userId = authService.signIn(signInRequest);
		TokenDto accessToken = jwtProvider.generateAccessToken(userId);
		TokenDto refreshToken  = jwtProvider.generateRefreshToken(userId);
		tokenService.setRefreshTokenCookie(refreshToken.getToken(), response);
		return ResponseUtil.success(TokenResponse.fromAccessTokenAndRefreshToken(accessToken, refreshToken));
	}

	@PostMapping("/signout")
	public SuccessResponse<?> signOut(@RequestBody SignOutRequest signOutRequest) {
		authService.singOut(signOutRequest.getAccessToken());
		return ResponseUtil.success();
	}

	@PostMapping("/reissue")
	public SuccessResponse<TokenResponse> reissue(@CookieValue(value = "refreshToken") String refreshToken) {
		TokenDto tokenDto = authService.reissue(refreshToken);
		return ResponseUtil.success(TokenResponse.fromAccessToken(tokenDto));
	}
}
