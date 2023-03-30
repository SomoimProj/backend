package com.oinzo.somoim.controller;

import com.oinzo.somoim.common.response.ResponseUtil;
import com.oinzo.somoim.common.response.SuccessResponse;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.controller.dto.FavoriteUpdateRequest;
import com.oinzo.somoim.controller.dto.UserInfoRequest;
import com.oinzo.somoim.controller.dto.UserInfoResponse;
import com.oinzo.somoim.domain.clubuser.service.ClubUserService;
import com.oinzo.somoim.domain.user.service.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final ClubUserService clubUserService;

	@GetMapping
	public SuccessResponse<UserInfoResponse> readUserInfo(@AuthenticationPrincipal Long userId) {
		UserInfoResponse userInfoResponse = userService.readUserInfo(userId);
		return ResponseUtil.success(userInfoResponse);
	}

	@PostMapping
	public SuccessResponse<UserInfoResponse> updateUserInfo(
		@AuthenticationPrincipal Long userId,
		@RequestBody @Valid UserInfoRequest request) {
		UserInfoResponse userInfoResponse = userService.updateUserInfo(userId, request);
		return ResponseUtil.success(userInfoResponse);
	}

	@PostMapping("/favorite")
	public SuccessResponse<?> updateFavorite(
		@AuthenticationPrincipal Long userId,
		@RequestBody @Valid FavoriteUpdateRequest request) {
		userService.updateFavorite(userId, request.getFavorites());
		return ResponseUtil.success();
	}

	// 자신이 가입한 클럽 조회
	@GetMapping("/join-clubs")
	public SuccessResponse<List<ClubResponse>> getJoinClubs(@AuthenticationPrincipal Long userId) {
		List<ClubResponse> clubs = clubUserService.getJoinClubs(userId);
		return ResponseUtil.success(clubs);
	}
}
