package com.oinzo.somoim.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.user.dto.GoogleUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GoogleOAuth2Service {

	@Value("${oauth2.google.client-id}")
	private String GOOGLE_CLIENT_ID;
	@Value("${oauth2.google.client-secret}")
	private String GOOGLE_CLIENT_SECRET;
	@Value("${service.front.domain}")
	private String FRONT_DOMAIN;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public GoogleOAuth2Service(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
	}

	public String getAccessToken(String code) {
		String accessTokenBody = requestAccessToken(code);
		return parseAccessToken(accessTokenBody);
	}

	public GoogleUserInfoDto getUserInfo(String accessToken) {
		String userInfoBody = requestUserInfo(accessToken);
		return parseUserInfo(userInfoBody);
	}

	private String requestAccessToken(String code) {
		String GOOGLE_ACCESS_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
		String GOOGLE_CODE_REDIRECT_URI = FRONT_DOMAIN + "/auth/google/callback";	// 인가코드 받았던 리다이렉트 주소

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", GOOGLE_CLIENT_ID);
		body.add("client_secret", GOOGLE_CLIENT_SECRET);
		body.add("redirect_uri", GOOGLE_CODE_REDIRECT_URI);
		body.add("code", code);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(
			GOOGLE_ACCESS_TOKEN_REQUEST_URL,
			HttpMethod.POST,
			request,
			String.class
		);

		return response.getBody();
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
		String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
			GOOGLE_USERINFO_REQUEST_URL,
			HttpMethod.GET,
			request,
			String.class
		);

		return response.getBody();
	}

	private GoogleUserInfoDto parseUserInfo(String responseBody) {
		try {
			JsonNode jsonNode = objectMapper.readTree(responseBody);

			String id = jsonNode.get("id").asText();

			String name = jsonNode.get("name").asText();
			String profileUrl = jsonNode.get("picture").asText();

			return GoogleUserInfoDto.builder()
				.googleId(id)
				.name(name)
				.profileUrl(profileUrl)
				.build();
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
