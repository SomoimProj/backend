package com.oinzo.somoim.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VerificationCodeResponse {

	private String code;

}
