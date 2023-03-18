package com.oinzo.somoim.common.jwt;

import static com.oinzo.somoim.common.jwt.JwtProperties.AUTHORIZATION_HEADER;
import static com.oinzo.somoim.common.jwt.JwtProperties.TOKEN_PREFIX;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtProvider jwtProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		String token = resolveToken((HttpServletRequest) request);

		if (StringUtils.hasText(token) && jwtProvider.isValidateToken(token)) {
			// 토큰 속 사용자 정보를 통해 만들어진 시큐리티 유저의 Authentication을 시큐리티 컨텍스트에 저장
			Authentication authentication = jwtProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}

	// Request Header에서 JWT 토큰 정보 추출
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(TOKEN_PREFIX.length() + 1);
		}
		return null;
	}
}
