package com.oinzo.somoim.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.domain.user.dto.KakaoUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoOAuth2Service {

	@Value("${oauth2.kakao.client-id}")
	private String KAKAO_CLIENT_ID;
	@Value("${service.front.domain}")
	private String FRONT_DOMAIN;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public KakaoOAuth2Service(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
	}

	public String getAccessToken(String code) {
		String accessTokenResponseBody = requestAccessToken(code);
		return parseAccessToken(accessTokenResponseBody);
	}

	public KakaoUserInfoDto getUserInfo(String accessToken) {
		String userInfoResponseBody = requestUserInfo(accessToken);
		return parseUserInfo(userInfoResponseBody);
	}

	private String requestAccessToken(String code) {
		String KAKAO_ACCESS_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
		String KAKAO_CODE_REDIRECT_URI = FRONT_DOMAIN + "/auth/kakao/callback";	// 인가코드 받았던 리다이렉트 주소

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", KAKAO_CLIENT_ID);
		body.add("redirect_uri", KAKAO_CODE_REDIRECT_URI);
		body.add("code", code);

		try {
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
			ResponseEntity<String> response = restTemplate.exchange(
				KAKAO_ACCESS_TOKEN_REQUEST_URL,
				HttpMethod.POST,
				request,
				String.class
			);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			handleAccessTokenRequestError(e);
			return "";	// handleAccessTokenRequestError()에서 모두 예외를 던지므로 여기까지 올 일 없음
		}
	}

	private void handleAccessTokenRequestError(HttpStatusCodeException httpStatusCodeException) {
		try {
			JsonNode jsonNode = objectMapper.readTree(httpStatusCodeException.getResponseBodyAsString());

			String errorCode = jsonNode.get("error_code").asText();
			String errorDescription = jsonNode.get("error_description").asText();
			log.error(errorCode + ": " + errorDescription);

			if (errorCode.equals("KOE320")) {    // 올바르지 않은 인가 코드
				throw new BaseException(ErrorCode.INVALID_KAKAO_CODE, errorDescription);
			} else if (errorCode.equals("KOE303")) {    // redirect uri 불일치
				throw new BaseException(ErrorCode.INVALID_KAKAO_REDIRECT_URI, errorDescription);
			} else {
				throw new BaseException(ErrorCode.KAKAO_ACCESS_TOKEN_REQUEST_ERROR, errorDescription);
			}
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private String parseAccessToken(String accessTokenResponseBody) {
		try {
			JsonNode jsonNode = objectMapper.readTree(accessTokenResponseBody);
			return jsonNode.get("access_token").asText();
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private String requestUserInfo(String accessToken) {
		String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		try {
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(
				KAKAO_USERINFO_REQUEST_URL,
				HttpMethod.POST,
				request,
				String.class
			);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			handleUserInfoRequestError(e);
			return "";	// handleUserInfoRequestError()에서 모두 예외를 던지므로 여기까지 올 일 없음
		}
	}

	private void handleUserInfoRequestError(HttpStatusCodeException httpStatusCodeException) {
		try {
			JsonNode jsonNode = objectMapper.readTree(httpStatusCodeException.getResponseBodyAsString());

			String errorCode = jsonNode.get("error_code").asText();
			String errorDescription = jsonNode.get("error_description").asText();
			log.error(errorCode + ": " + errorDescription);

			throw new BaseException(ErrorCode.KAKAO_USERINFO_REQUEST_ERROR, errorDescription);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private KakaoUserInfoDto parseUserInfo(String responseBody) {
		try {
			JsonNode jsonNode = objectMapper.readTree(responseBody);

			long id = jsonNode.get("id").asLong();

			JsonNode kakaoAccountNode = jsonNode.get("kakao_account");
			String profileUrl = null;
			if (kakaoAccountNode.has("profile") && kakaoAccountNode.get("profile").has("profile_image_url")) {
				profileUrl = kakaoAccountNode.get("profile").get("profile_image_url").asText();
			}
			String gender = null;
			if (kakaoAccountNode.has("gender")) {
				gender = kakaoAccountNode.get("gender").asText();
			}
			String birthday = null;
			if (kakaoAccountNode.has("birthday")) {
				birthday = kakaoAccountNode.get("birthday").asText();
			}

			return KakaoUserInfoDto.builder()
				.kakaoId(id)
				.profileUrl(profileUrl)
				.gender(gender == null ? null : gender.equals("male") ? Gender.MALE : Gender.FEMALE)    // Gender enum으로 변경
				.birthday(birthday)
				.build();
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
