package com.oinzo.somoim.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.response.ErrorResponse;
import com.oinzo.somoim.common.response.ResponseUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

// 권한 확인 과정에서 거부된 경우 핸들링
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		ErrorResponse errorResponse = ResponseUtil.error(ErrorCode.FORBIDDEN_REQUEST);
		String responseJson = objectMapper.writeValueAsString(errorResponse);

		response.getWriter().print(responseJson);	// 한글 출력을 위해 getWriter() 사용
	}
}
