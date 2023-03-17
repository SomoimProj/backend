package com.oinzo.somoim.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String detail;

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}
}
