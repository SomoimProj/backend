package com.oinzo.somoim.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	/* 400 Bad Request */
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, 40002, "잘못된 토큰입니다."),

	/* 401 Unauthorized */
	ACCESS_TOKEN_OMISSION(HttpStatus.UNAUTHORIZED, 40101, "인증 정보(액세스 토큰)가 누락되었습니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 40103, "만료된 액세스 토큰입니다."),

	/* 403 Forbidden */
	FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, 40300, "해당 요청에 대한 권한이 없습니다."),

	/* 404 Not Found */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, 40401, "사용자를 찾을 수 없습니다."),

	/* 500 Internal Server Error */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "예상치 못한 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;
}
