package com.oinzo.somoim.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ErrorController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BaseException.class)
	protected ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
		log.warn("message: {}, detail: {}", e.getMessage(), e.getDetail(), e);
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(new ErrorResponse(e.getErrorCode(), e.getDetail()));
	}

	// 디버깅을 위해 주석 처리
	/*@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	protected ErrorResponse handleRuntimeException(RuntimeException e) {
		log.error(e.getMessage(), e);
		return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
	}*/
}
