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
	CLUB_LIMIT_OVER(HttpStatus.BAD_REQUEST, 40005, "해당 클럽의 멤버 정원이 다 찼습니다."),
	ALREADY_CLUB_MEMBER(HttpStatus.BAD_REQUEST, 40006, "이미 해당 클럽의 멤버입니다."),
	WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 40008, "비밀번호가 일치하지 않습니다."),
	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, 40009, "입력값 유효성 검사에 실패하였습니다."),
	NOT_SET_AREA(HttpStatus.BAD_REQUEST, 40010, "사용자의 지역이 설정되지 않았습니다"),
	ALREADY_LIKED(HttpStatus.BAD_REQUEST, 40030, "이미 좋아요 한 게시물 입니다."),
	ALREADY_JOINED_ACTIVITY(HttpStatus.BAD_REQUEST, 40031, "이미 참여한 액티비티 입니다."),
	ACTIVITY_LIMIT_OVER(HttpStatus.BAD_REQUEST, 40032, "해당 액티비티의 정원이 다 찼습니다."),

	/* 401 Unauthorized */
	ACCESS_TOKEN_OMISSION(HttpStatus.UNAUTHORIZED, 40101, "인증 정보(액세스 토큰)가 누락되었습니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 40103, "만료된 액세스 토큰입니다."),
	NOT_CLUB_MEMBER(HttpStatus.NOT_FOUND, 40130, "클럽에 가입후 이용 가능합니다."),
	NOT_CLUB_MANAGER(HttpStatus.NOT_FOUND, 40131, "클럽 관리자만 이용 가능합니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 40161, "만료된 리프레쉬 토큰입니다."),
	LOGOUT_USER(HttpStatus.UNAUTHORIZED, 40162, "로그아웃된 사용자입니다."),
	WRONG_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 40163, "리프레쉬 토큰 정보가 유효하지 않습니다."),


	/* 403 Forbidden */
	FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, 40300, "해당 요청에 대한 권한이 없습니다."),

	/* 404 Not Found */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, 40401, "사용자를 찾을 수 없습니다."),
	NO_DATA_FOUND(HttpStatus.NOT_FOUND, 40402, "해당하는 데이터를 찾을 수 없습니다."),
	WRONG_CLUB(HttpStatus.NOT_FOUND, 40403, "해당하는 클럽을 찾을 수 없습니다."),
	WRONG_FAVORITE(HttpStatus.NOT_FOUND, 40404, "해당하는 관심사를 찾을 수 없습니다."),
	WRONG_BOARD(HttpStatus.NOT_FOUND, 40430, "해당하는 게시글을 찾을 수 없습니다."),
	WRONG_CATEGORY(HttpStatus.NOT_FOUND, 40431, "해당하는 카테고리를 찾을 수 없습니다."),
	WRONG_COMMENT(HttpStatus.NOT_FOUND, 40432, "해당하는 댓글을 찾을 수 없습니다."),
	WRONG_ALBUM(HttpStatus.NOT_FOUND, 40433, "해당하는 사진을 찾을 수 없습니다."),
	WRONG_LIKE(HttpStatus.NOT_FOUND, 40434, "해당하는 좋아요를 찾을 수 없습니다."),
	WRONG_ACTIVITY(HttpStatus.NOT_FOUND, 40435, "해당하는 활동을 찾을 수 없습니다."),
	WRONG_ACTIVITY_USER(HttpStatus.NOT_FOUND, 40436, "해당유저가 참여한 활동이 아닙니다."),

	/* 409 Conflict */
	ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, 40901, "이미 가입된 이메일 입니다."),

	/* 500 Internal Server Error */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "예상치 못한 오류가 발생했습니다."),
	INVALID_KAKAO_REDIRECT_URI(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "리다이렉트 주소가 일치하지 않습니다. 카카오 인가 코드 요청 시 사용한 리다이렉트 주소를 백엔드 담당자에게 알려주세요."),
	KAKAO_ACCESS_TOKEN_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "인가 코드를 통해 카카오 액세스 토큰을 발급받는 중에 문제가 발생했습니다."),
	KAKAO_USERINFO_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50003, "액세스 토큰을 통해 카카오 사용자 정보를 조회하는 과정에서 문제가 발생했습니다."),
	FAILED_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, 50004, "메일을 전송하는 과정에서 문제가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;
}

