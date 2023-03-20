package com.oinzo.somoim.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/* 400 Bad Request */
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, 40002, "잘못된 토큰입니다."),
	INVALID_KAKAO_CODE(HttpStatus.BAD_REQUEST, 40003, "올바르지 않은 카카오 인가 코드입니다."),
  NO_SEARCH_NAME(HttpStatus.BAD_REQUEST, 40004, "검색 키워드를 입력해주십시오."),

	/* 401 Unauthorized */
	ACCESS_TOKEN_OMISSION(HttpStatus.UNAUTHORIZED, 40101, "인증 정보(액세스 토큰)가 누락되었습니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 40103, "만료된 액세스 토큰입니다."),

	/* 403 Forbidden */
	FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, 40300, "해당 요청에 대한 권한이 없습니다."),

	/* 404 Not Found */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, 40401, "사용자를 찾을 수 없습니다."),
  	NO_DATA_FOUND(HttpStatus.NOT_FOUND, 40402, "해당하는 데이터를 찾을 수 없습니다."),
	WRONG_CLUB(HttpStatus.NOT_FOUND, 40403, "해당하는 클럽을 찾을 수 없습니다."),
	WRONG_FAVORITE(HttpStatus.NOT_FOUND, 40404, "해당하는 관심사를 찾을 수 없습니다."),

	/* 500 Internal Server Error */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "예상치 못한 오류가 발생했습니다."),
	INVALID_KAKAO_REDIRECT_URI(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "리다이렉트 주소가 일치하지 않습니다. 카카오 인가 코드 요청 시 사용한 리다이렉트 주소를 백엔드 담당자에게 알려주세요."),
	KAKAO_ACCESS_TOKEN_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "인가 코드를 통해 카카오 액세스 토큰을 발급받는 중에 문제가 발생했습니다."),
	KAKAO_USERINFO_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50003, "액세스 토큰을 통해 카카오 사용자 정보를 조회하는 과정에서 문제가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;
}
