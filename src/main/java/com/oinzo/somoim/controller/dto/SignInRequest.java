package com.oinzo.somoim.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {

	@Email
	private String email;
	@NotBlank
	private String password;
}
