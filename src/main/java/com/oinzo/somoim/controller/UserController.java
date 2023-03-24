package com.oinzo.somoim.controller;

import com.oinzo.somoim.controller.dto.FavoriteUpdateRequest;
import com.oinzo.somoim.controller.dto.UserInfoRequest;
import com.oinzo.somoim.controller.dto.UserInfoResponse;
import com.oinzo.somoim.domain.user.service.UserService;
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

	@GetMapping
	public UserInfoResponse readUserInfo(@AuthenticationPrincipal Long userId) {
		return userService.readUserInfo(userId);
	}

	@PostMapping
	public UserInfoResponse updateUserInfo(
		@AuthenticationPrincipal Long userId,
		@RequestBody @Valid UserInfoRequest request) {
		return userService.updateUserInfo(userId, request);
	}

	@PostMapping("/favorite")
	public void updateFavorite(
		@AuthenticationPrincipal Long userId,
		@RequestBody @Valid FavoriteUpdateRequest request) {
		userService.updateFavorite(userId, request.getFavorite());
	}
}
