package com.oinzo.somoim.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.oinzo.somoim.common.jwt.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.oinzo.somoim.common.jwt.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

@Slf4j
@Component
public class JwtProvider {

	private final UserDetailsService userDetailsService;
	@Autowired
	private final RedisTemplate<String, String> redisTemplate;


	private final Key secretKey;
	private final String TOKEN_PREFIX = "RTK:";


	public JwtProvider(
		UserDetailsService userDetailsService,
		RedisTemplate<String, String> redisTemplate,
		@Value("${jwt.secret}") String secretKey
	) {
		this.userDetailsService = userDetailsService;
		this.redisTemplate = redisTemplate;

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

		redisTemplate.opsForValue().set(
			TOKEN_PREFIX + userId,
			refreshToken,
			JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME,
			TimeUnit.MILLISECONDS
		);

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
			log.error("잘못된 JWT 토큰입니다.", e);
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰입니다.", e);
		} catch (UnsupportedJwtException e) {
			log.error("지원하지 않는 JWT 토큰 형식입니다.", e);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims 값이 없습니다.", e);
		}
		return false;
	}
}