package com.oinzo.somoim.common.jwt;

import static com.oinzo.somoim.common.jwt.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.oinzo.somoim.common.jwt.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

import com.oinzo.somoim.common.exception.BaseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

	private final UserDetailsService userDetailsService;

	private final Key secretKey;

	public JwtProvider(
		UserDetailsService userDetailsService,
		@Value("${jwt.secret}") String secretKey
	) {
		this.userDetailsService = userDetailsService;

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	// JWT Access Token, Refresh Token 발급
	public TokenDto generateAccessTokenAndRefreshToken(Long userId) {
		Date now = new Date();

		Date accessTokenExpirationIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
		String accessToken = Jwts.builder()
			.setSubject(String.valueOf(userId))
			.setIssuedAt(now)
			.setExpiration(accessTokenExpirationIn)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();

		Date refreshTokenExpirationIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);
		String refreshToken = Jwts.builder()
			.setSubject(String.valueOf(userId))
			.setIssuedAt(now)
			.setExpiration(refreshTokenExpirationIn)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();

		// TODO: Redis 서버에 저장

		return TokenDto.builder()
			.accessToken(accessToken)
			.accessTokenExpirationIn(accessTokenExpirationIn)
			.refreshToken(refreshToken)
			.refreshTokenExpirationIn(refreshTokenExpirationIn)
			.build();
	}

	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String accessToken) {
		String userId = this.getUserId(accessToken);
		UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
		return new UsernamePasswordAuthenticationToken(
			Long.parseLong(userId),
			"",
			userDetails.getAuthorities());
	}

	// JWT 토큰에서 회원 구별 정보 추출
	private String getUserId(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build()
			.parseClaimsJws(token).getBody().getSubject();
	}

	// JWT 토큰 검증
	public boolean isValidateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty.", e);
		}
		return false;
	}
}
