package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.user.dto.EmailDto;
import com.oinzo.somoim.domain.user.dto.SignUpDto;
import com.oinzo.somoim.domain.user.email.EmailService;
import com.oinzo.somoim.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RequestMapping("/users/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final EmailService emailService;
	private final UserService userService;

	@PostMapping("/email/send")
	public String sendMail(@RequestBody EmailDto emailDto) throws MessagingException {
		String code = emailService.sendMail(emailDto.getEmail());

		return code;
	}

	@PostMapping("/email/check")
	public boolean checkCode(@RequestBody EmailDto emailDto) {
		boolean result = emailService.checkVerificationCode(emailDto.getEmail(), emailDto.getCode());

		return result;
	}

	@PostMapping("/signup")
	public void signUp(@RequestBody SignUpDto signUpDto) {
		userService.signUp(signUpDto.getEmail(), signUpDto.getPassword(), signUpDto.getPasswordCheck());
	}
}
