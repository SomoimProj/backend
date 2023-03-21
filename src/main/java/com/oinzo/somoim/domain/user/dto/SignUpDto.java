package com.oinzo.somoim.domain.user.dto;

import com.oinzo.somoim.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDto {

	private String email;
	private String password;
	private String passwordCheck;

	public User toEntity(PasswordEncoder passwordEncoder, SignUpDto signUpDto) {
		return User.builder()
			.email(signUpDto.getEmail())
			.password(passwordEncoder.encode(signUpDto.getPassword()))
			.build();
	}
}