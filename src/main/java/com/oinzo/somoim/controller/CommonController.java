package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.common.service.CloudflareService;
import com.oinzo.somoim.controller.dto.UploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommonController {

	private final CloudflareService cloudflareService;

	// Cloudflare 이미지업로드 url 반환
	@GetMapping("/upload-url")
	public SuccessResponse<UploadUrlResponse> getUploadUrlResponse() {
		String uploadUrl = cloudflareService.getUploadUrl();
		UploadUrlResponse uploadUrlResponse = UploadUrlResponse.builder()
			.url(uploadUrl)
			.build();
		return ResponseUtil.success(uploadUrlResponse);
	}

}
