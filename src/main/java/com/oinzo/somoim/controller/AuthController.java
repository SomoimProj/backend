package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.common.jwt.TokenService;
import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.CheckResponse;
import com.oinzo.somoim.controller.dto.EmailRequest;
import com.oinzo.somoim.controller.dto.SignInRequest;
import com.oinzo.somoim.controller.dto.SignUpRequest;
import com.oinzo.somoim.controller.dto.TokenResponse;
import com.oinzo.somoim.controller.dto.VerificationCodeResponse;
import com.oinzo.somoim.domain.user.email.EmailService;
import com.oinzo.somoim.domain.user.service.AuthService;
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
		return ResponseUtil.success();
	}

	@PostMapping("/signin")
	public SuccessResponse<TokenResponse> signIn(@RequestBody @Valid SignInRequest signInRequest,
								HttpServletResponse response) {
		Long userId = authService.signIn(signInRequest);
		TokenDto tokenDto = jwtProvider.generateAccessTokenAndRefreshToken(userId);
		String refreshToken = tokenDto.getRefreshToken();
		tokenService.setRefreshTokenCookie(refreshToken, response);
		return ResponseUtil.success(TokenResponse.from(tokenDto));
	}


	@PostMapping("/signout")
	public SuccessResponse<?> signOut(@RequestBody TokenDto tokenDto) {
		authService.singOut(tokenDto);
		return ResponseUtil.success();
	}

	/**
	 * TODO: 토큰재발급
	 */
//	@PostMapping("/reissue")
//	public ResponseEntity<String> regenerateToken(@RequestBody RegenerateTokenDto regenerateTokenDto) {
//
//		return authService.regenerateToken(regenerateTokenDto);
//	}

}
