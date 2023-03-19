package com.oinzo.somoim.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/* 100 Request Error */
	NO_SEARCH_NAME(HttpStatus.NOT_FOUND, 10001, "검색 키워드를 입력해주십시오."),

	NO_DATA_FOUND(HttpStatus.NOT_FOUND, 40001, "해당하는 데이터를 찾을 수 없습니다."),
	WRONG_CLUB(HttpStatus.NOT_FOUND, 40002, "해당하는 클럽을 찾을 수 없습니다."),

	WRONG_FAVORITE(HttpStatus.NOT_FOUND, 40005, "해당하는 관심사를 찾을 수 없습니다."),
	/* 500 Internal Server Error */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "예상치 못한 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;
}
