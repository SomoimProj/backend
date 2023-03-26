package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.common.jwt.TokenService;
import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.TokenResponse;
import com.oinzo.somoim.domain.user.dto.EmailDto;
import com.oinzo.somoim.domain.user.dto.SignInDto;
import com.oinzo.somoim.domain.user.dto.SignUpDto;
import com.oinzo.somoim.domain.user.email.EmailService;
import com.oinzo.somoim.domain.user.service.AuthService;
import javax.mail.MessagingException;
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
	public SuccessResponse<String> sendMail(@RequestBody EmailDto emailDto) throws MessagingException {
		String verificationCode = emailService.sendMail(emailDto.getEmail());
		return ResponseUtil.success(verificationCode);
	}

	@PostMapping("/email/check")
	public SuccessResponse<Boolean> checkCode(@RequestBody EmailDto emailDto) {
		boolean result = emailService.checkVerificationCode(emailDto);
		return ResponseUtil.success(result);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/signup")
	public SuccessResponse<?> signUp(@RequestBody SignUpDto signUpDto) {
		authService.signUp(signUpDto.getEmail(), signUpDto.getPassword(), signUpDto.getPasswordCheck());
		return ResponseUtil.success();
	}

	@PostMapping("/signin")
	public SuccessResponse<TokenResponse> signIn(@RequestBody @Valid SignInDto signInDto,
								HttpServletResponse response) {
		Long userId = authService.signIn(signInDto);
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
