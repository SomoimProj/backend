package com.oinzo.somoim.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.response.ErrorResponse;
import com.oinzo.somoim.common.response.ResponseUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// JWT 인증 과정에서 발생한 예외 핸들링
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(
		HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		Integer exceptionCode = (Integer) request.getAttribute("exception");

		if (exceptionCode == ErrorCode.ACCESS_TOKEN_OMISSION.getCode()) {
			setErrorResponse(response, ErrorCode.ACCESS_TOKEN_OMISSION);
			return;
		}

		if (exceptionCode == ErrorCode.EXPIRED_ACCESS_TOKEN.getCode()) {
			setErrorResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
			return;
		}

		if (exceptionCode == ErrorCode.INVALID_TOKEN.getCode()) {
			setErrorResponse(response, ErrorCode.INVALID_TOKEN);
			return;
		}

		if (exceptionCode == ErrorCode.USER_NOT_FOUND.getCode()) {
			setErrorResponse(response, ErrorCode.USER_NOT_FOUND);
			return;
		}

		if (exceptionCode == null) {
			log.error("예상하지 못한 JWT 인증 문제가 발생했습니다.", authException);
			setErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public void setErrorResponse(HttpServletResponse response, ErrorCode errorCode)
		throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ErrorResponse errorResponse = ResponseUtil.error(errorCode);
		String responseJson = objectMapper.writeValueAsString(errorResponse);

		response.getWriter().print(responseJson);	// 한글 출력을 위해 getWriter() 사용
	}
}
