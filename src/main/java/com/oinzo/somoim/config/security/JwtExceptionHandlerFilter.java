package com.oinzo.somoim.config.security;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

// JwtAuthenticationFilter에서 발생하는 에러 핸들링
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			// 다음 filter chain 실행
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰입니다.");
			request.setAttribute("exception", ErrorCode.EXPIRED_ACCESS_TOKEN.getCode());
		} catch (JwtException | IllegalArgumentException e) {
			log.error("잘못된 JWT 토큰입니다.");
			request.setAttribute("exception", ErrorCode.INVALID_TOKEN.getCode());
		} catch (NoSuchElementException e) {
			log.warn("JWT 토큰 정보가 없습니다.");
			request.setAttribute("exception", ErrorCode.ACCESS_TOKEN_OMISSION.getCode());
		} catch (BaseException e) {
			if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
				log.error("사용자를 찾을 수 없습니다.", e);
				request.setAttribute("exception", ErrorCode.USER_NOT_FOUND.getCode());
			}
		}

		filterChain.doFilter(request, response);
	}
}
