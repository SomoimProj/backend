package com.oinzo.somoim.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String detail;

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}

	public BaseException(ErrorCode errorCode, String detail) {
		this.errorCode = errorCode;
		this.detail = detail;
	}

	public BaseException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.detail = "";
	}
}
