package com.oinzo.somoim.domain.user.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.jwt.JwtProperties;
import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.common.redis.RedisService;
import com.oinzo.somoim.config.security.JwtAuthenticationFilter;
import com.oinzo.somoim.controller.dto.SignInRequest;
import com.oinzo.somoim.controller.dto.SignOutRequest;
import com.oinzo.somoim.controller.dto.TokenResponse;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

	private final UserRepository userRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;


	private String passwordEncode(String password) {
		return passwordEncoder.encode(password);
	}

	public void signUp(String email, String password, String passwordCheck) {

		if (userRepository.existsByEmail(email)) {
			throw new BaseException(ErrorCode.ALREADY_EXISTS_EMAIL);
		}

		if (!password.equals(passwordCheck)) {
			throw new BaseException(ErrorCode.WRONG_PASSWORD);
		}

		User user = User.builder()
			.email(email)
			.password(passwordEncode(password))
			.build();

		userRepository.save(user);
	}

	public Long signIn(SignInRequest signInRequest) {

		User user = userRepository.findByEmail(signInRequest.getEmail())
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
		String password = user.getPassword();

		if (!passwordEncoder.matches(signInRequest.getPassword(), password)) {
			throw new BaseException(ErrorCode.WRONG_PASSWORD);
		}

		return user.getId();
	}

	public void singOut(SignOutRequest request) {
		String accessToken = request.getAccessToken();

		if (!jwtProvider.isValidateToken(accessToken)) {
			throw new BaseException(ErrorCode.INVALID_TOKEN);
		}

		Authentication authentication = jwtProvider.getAuthentication(accessToken);

		String key = JwtProperties.REFRESH_TOKEN_PREFIX + authentication.getName();
		if (redisTemplate.opsForValue().get(key) != null) {
			redisTemplate.delete(key);
		}

		// 블랙리스트에 accessToken 등록
		redisService.setBlackList(accessToken, "accessToken", 30);
	}

	/**
	 * 토큰 재발급
	 */
	public TokenDto reissue(String refreshToken) {
		System.out.println("refreshToken = " + refreshToken);
		if (!jwtProvider.isValidateToken(refreshToken)) {
			throw new BaseException(ErrorCode.INVALID_TOKEN, "refreshToken 이 유효하지 않습니다.");
		}

		Authentication authentication = jwtProvider.getAuthentication(refreshToken);
		Long userId = (Long) authentication.getPrincipal();

		if (userId == null) {
			throw new BaseException(ErrorCode.INVALID_TOKEN, "refreshToken 이 유효하지 않습니다.");
		}

		String refreshTokenInRedis = (String) redisService.get(JwtProperties.REFRESH_TOKEN_PREFIX + userId);
		if (!refreshTokenInRedis.equals(refreshToken)) {
			throw new BaseException(ErrorCode.INVALID_TOKEN, "refreshToken 이 일치하지 않습니다.");
		}

		return jwtProvider.generateAccessToken(userId);
	}

}
