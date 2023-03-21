package com.oinzo.somoim.domain.user.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.jwt.JwtProperties;
import com.oinzo.somoim.common.jwt.JwtProvider;
import com.oinzo.somoim.common.jwt.TokenDto;
import com.oinzo.somoim.domain.user.dto.SignInDto;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final UserRepository userRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final String TOKEN_PREFIX = "RTK:";

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

	public ResponseEntity<TokenDto> signIn(SignInDto signInDto) {

		if (!userRepository.existsByEmail(signInDto.getEmail())) {
			throw new BaseException(ErrorCode.USER_NOT_FOUND);
		}

		Optional<User> user = userRepository.findByEmail(signInDto.getEmail());
		String password = user.get().getPassword();

		if (!passwordEncoder.matches(signInDto.getPassword(), password)) {
			throw new BaseException(ErrorCode.WRONG_PASSWORD);
		}

		Long userId = userRepository.findByEmail(signInDto.getEmail()).get().getId();

		String accessToken = jwtProvider.generateAccessTokenAndRefreshToken(userId).getAccessToken();
		String refreshToken = jwtProvider.generateAccessTokenAndRefreshToken(userId).getRefreshToken();

		System.out.println("refreshToken = " + refreshToken);
		System.out.println("accessToken = " + accessToken);

		TokenDto tokenDto = TokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();

		redisTemplate.opsForValue().set(
			TOKEN_PREFIX + userId,
			refreshToken,
			JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME,
			TimeUnit.MILLISECONDS
		);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtProperties.AUTHORIZATION_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);

		return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
	}

	public void singOut(TokenDto tokenDto) {
		if (!jwtProvider.isValidateToken(tokenDto.getAccessToken())) {
			throw new BaseException(ErrorCode.INVALID_TOKEN);
		}

		Authentication authentication = jwtProvider.getAuthentication(tokenDto.getAccessToken());

		String key = TOKEN_PREFIX + authentication.getName();

		if (redisTemplate.opsForValue().get(key) != null) {
			redisTemplate.delete(key);
		}

		// todo : AT 유효시간을 가져와서 블랙리스트로 저장하기
	}

	/**
	 * TODO : 토큰 재발급
	 */
//	public ResponseEntity<String> regenerateToken(RegenerateTokenDto regenerateTokenDto) {
//		Authentication authentication = jwtProvider.getAuthentication(regenerateTokenDto.getRefreshToken());
//
//		if (!jwtProvider.isValidateToken(redisTemplate.opsForValue().get(PREFIX+authentication.getName()))) {
//			throw new BaseException(ErrorCode.WRONG_REFRESH_TOKEN);
//		}
//
//		String refreshToken = redisTemplate.opsForValue().get(PREFIX + authentication.getName());
//
//		if (ObjectUtils.isEmpty(refreshToken)) {
//			throw new BaseException(ErrorCode.INVALID_TOKEN);
//		}
//
//		if (!refreshToken.equals(regenerateTokenDto.getRefreshToken())) {
//			throw new BaseException(ErrorCode.WRONG_REFRESH_TOKEN);
//		}
//
//		Optional<User> user = userRepository.findByEmail(authentication.getName());
//		Long userId = user.get().getId();
//		TokenDto newToken = jwtProvider.generateAccessTokenAndRefreshToken(userId);
//
//		redisTemplate.opsForValue()
//			.set(PREFIX + authentication.getName(), newToken.getRefreshToken(), JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
//
//		return ResponseEntity.ok(newToken.getRefreshToken());
//	}


}