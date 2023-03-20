package com.oinzo.somoim.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* 400 Bad Request */
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 40002, "잘못된 토큰입니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, 40007, "입력한 인증번호가 일치하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 40008, "비밀번호 확인이 일치하지 않습니다."),

    /* 401 Unauthorized */
    ACCESS_TOKEN_OMISSION(HttpStatus.UNAUTHORIZED, 40101, "인증 정보(액세스 토큰)가 누락되었습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 40103, "만료된 액세스 토큰입니다."),

    /* 403 Forbidden */
    FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, 40300, "해당 요청에 대한 권한이 없습니다."),

    /* 404 Not Found */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 40401, "사용자를 찾을 수 없습니다."),

    /* 409 Conflict */
    ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, 40901, "이미 가입된 이메일 입니다."),

    /* 500 Internal Server Error */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "예상치 못한 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
