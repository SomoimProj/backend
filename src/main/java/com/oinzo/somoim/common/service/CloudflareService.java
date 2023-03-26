package com.oinzo.somoim.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CloudflareService {

	@Value("${cloudflare.access-token}")
	private String CLOUDFLARE_ACCESS_TOKEN;
	@Value("${cloudflare.account-identifier}")
	private String CLOUDFLARE_ACCOUNT_IDENTIFIER;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public CloudflareService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
	}

	public String getUploadUrl() {
		String uploadUrlResponseBody = requestUploadUrl();
		return parseUploadUrl(uploadUrlResponseBody);
	}

	private String requestUploadUrl() {
		String CLOUDFLARE_UPLOAD_URL_REQUEST_URL = "https://api.cloudflare.com/client/v4/accounts/"
				+ CLOUDFLARE_ACCOUNT_IDENTIFIER + "/images/v2/direct_upload";

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + CLOUDFLARE_ACCESS_TOKEN);

		try {
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(
				CLOUDFLARE_UPLOAD_URL_REQUEST_URL,
				HttpMethod.POST,
				request,
				String.class
			);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			handleUploadUrlRequestError(e);
			return "";	// handleUploadUrlRequestError()에서 모두 예외를 던지므로 여기까지 올 일 없음
		}
	}

	private void handleUploadUrlRequestError(HttpStatusCodeException httpStatusCodeException) {
		try {
			JsonNode jsonNode = objectMapper.readTree(httpStatusCodeException.getResponseBodyAsString());

			StringBuilder errorMessageBuilder = new StringBuilder();
			Iterator<JsonNode> errors = jsonNode.get("errors").elements();
			if (errors.hasNext()) {
				JsonNode errorNode = errors.next();
				String errorCode = errorNode.get("code").asText();
				String errorMessage = errorNode.get("message").asText();
				log.error(errorCode + ": " + errorMessage);
				errorMessageBuilder.append(errorMessage);
			}

			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, errorMessageBuilder.toString());
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private String parseUploadUrl(String uploadUrlResponseBody) {
		try {
			JsonNode jsonNode = objectMapper.readTree(uploadUrlResponseBody);
			return jsonNode.get("result").get("uploadURL").asText();
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
