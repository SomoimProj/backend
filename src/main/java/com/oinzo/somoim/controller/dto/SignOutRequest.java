package com.oinzo.somoim.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class SignOutRequest {

	@NotBlank
	private final String accessToken;
}
