package com.oinzo.somoim.domain.user.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.controller.dto.UserInfoRequest;
import com.oinzo.somoim.controller.dto.UserInfoResponse;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;


	public UserInfoResponse readUserInfo(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));

		return UserInfoResponse.from(user);

	}

	public UserInfoResponse updateUserInfo(Long userId, UserInfoRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));

		user.updateUserInfo(request);
		User savedUser = userRepository.save(user);
		return UserInfoResponse.from(savedUser);
	}

	public void updateFavorite(Long userId, String favoriteString) {
		Favorite favorite = Favorite.valueOfOrHandleException(favoriteString);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));

		user.updateFavorite(favorite);
		userRepository.save(user);
	}
}
