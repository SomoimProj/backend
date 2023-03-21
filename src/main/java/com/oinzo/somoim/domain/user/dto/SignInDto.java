package com.oinzo.somoim.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInDto {

	private String email;
	private String password;
}
