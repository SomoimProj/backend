package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.user.entity.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {

	@Email
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String passwordCheck;

	public User toEntity(PasswordEncoder passwordEncoder, SignUpRequest signUpRequest) {
		return User.builder()
			.email(signUpRequest.getEmail())
			.password(passwordEncoder.encode(signUpRequest.getPassword()))
			.build();
	}
}
