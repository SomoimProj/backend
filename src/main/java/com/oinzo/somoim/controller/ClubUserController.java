package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.controller.dto.MemberResponse;
import com.oinzo.somoim.domain.clubuser.service.ClubUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ClubUserController {

	private final ClubUserService clubUserService;

	// 클럽 가입
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/clubs/{clubId}/join")
	public SuccessResponse<?> joinClub(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long clubId) {
		clubUserService.joinClub(userId, clubId);
		return ResponseUtil.success();
	}

	// 클럽 멤버 조회
	@GetMapping("/clubs/{clubId}/members")
	public SuccessResponse<List<MemberResponse>> getMembers(@PathVariable Long clubId) {
		List<MemberResponse> members = clubUserService.getMembers(clubId);
		return ResponseUtil.success(members);
	}

	// 자신이 가입한 클럽 조회
	@GetMapping("/users/join-clubs")
	public SuccessResponse<List<ClubResponse>> getJoinClubs(@AuthenticationPrincipal Long userId) {
		List<ClubResponse> clubs = clubUserService.getJoinClubs(userId);
		return ResponseUtil.success(clubs);
	}

}
