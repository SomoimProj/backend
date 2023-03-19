package com.oinzo.somoim.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/* 400 Database Error */
	NO_DATA_FOUND(HttpStatus.NOT_FOUND, 40001, "해당하는 데이터를 찾을 수 없습니다."),
	WRONG_CLUB(HttpStatus.NOT_FOUND, 40002, "해당하는 클럽을 찾을 수 없습니다."),
	/* 500 Internal Server Error */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "예상치 못한 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;
}
