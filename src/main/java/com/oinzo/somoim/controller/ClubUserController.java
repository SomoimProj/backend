package com.oinzo.somoim.controller;

import com.oinzo.somoim.controller.dto.JoinClubRequest;
import com.oinzo.somoim.domain.clubuser.service.ClubUserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ClubUserController {

	private final ClubUserService clubUserService;

	// 클럽 가입
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/clubs/{clubId}/join")
	public void joinClub(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long clubId,
		@RequestBody @Valid JoinClubRequest request) {
		clubUserService.joinClub(userId, clubId, request.getIntroduction());
	}

}
