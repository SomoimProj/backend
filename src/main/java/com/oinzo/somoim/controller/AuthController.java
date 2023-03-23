package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.common.jwt.TokenService;
import com.oinzo.somoim.controller.dto.EmailResponse;
import com.oinzo.somoim.controller.dto.TokenResponse;
import com.oinzo.somoim.domain.user.dto.EmailDto;
import com.oinzo.somoim.domain.user.dto.SignInDto;
import com.oinzo.somoim.domain.user.dto.SignUpDto;
import com.oinzo.somoim.domain.user.service.AuthService;
import com.oinzo.somoim.domain.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
	public EmailResponse sendMail(@RequestBody EmailDto emailDto) throws MessagingException {
		String code = emailService.sendMail(emailDto.getEmail());

		return EmailResponse.builder()
			.inputCode(emailDto.getCode())
			.verificationCode(code)
			.build();
	}

	@PostMapping("/email/check")
	public boolean checkCode(@RequestBody EmailDto emailDto) {

		return emailService.checkVerificationCode(emailDto);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/signup")
	public TokenResponse signUp(@RequestBody SignUpDto signUpDto, HttpServletResponse response) {
		Long userId = authService.signUp(signUpDto.getEmail(),
			signUpDto.getPassword(), signUpDto.getPasswordCheck());

		TokenDto tokenDto = jwtProvider.generateAccessTokenAndRefreshToken(userId);
		String refreshToken = tokenDto.getRefreshToken();
		tokenService.setRefreshTokenCookie(refreshToken, response);
		return TokenResponse.from(tokenDto);
	}

	@PostMapping("/signin")
	public TokenResponse signIn(@RequestBody @Valid SignInDto signInDto,
								HttpServletResponse response) {
		Long userId = authService.signIn(signInDto);
		TokenDto tokenDto = jwtProvider.generateAccessTokenAndRefreshToken(userId);
		String refreshToken = tokenDto.getRefreshToken();
		tokenService.setRefreshTokenCookie(refreshToken, response);
		return TokenResponse.from(tokenDto);
	}


	@PostMapping("/signout")
	public void signout(@RequestBody TokenDto tokenDto) {
		authService.singOut(tokenDto);
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