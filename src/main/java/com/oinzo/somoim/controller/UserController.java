package com.oinzo.somoim.controller;

import com.oinzo.somoim.controller.dto.UserInfoResponse;
import com.oinzo.somoim.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

}
