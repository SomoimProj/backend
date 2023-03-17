package com.oinzo.somoim.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"code", "message", "detail"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	private int code;
	private String message;
	private String detail;

	public ErrorResponse(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.detail = null;
	}

	public ErrorResponse(ErrorCode errorCode, String detail) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.detail = detail;
	}
}
