package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.domain.clublike.service.ClubLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ClubLikeController {

	private final ClubLikeService clubLikeService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/clubs/{clubId}/likes")
	public SuccessResponse<?> addLike(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long clubId) {
		clubLikeService.addLike(userId, clubId);
		return ResponseUtil.success();
	}

}
