package com.oinzo.somoim.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.controller.dto.UserInfoRequest;
import com.oinzo.somoim.controller.dto.UserInfoResponse;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	private UserRepository userRepository;

	@Test
	public void testReadUserInfo() {
		// given
		User mockUser = User.builder()
			.id(1L)
			.name("홍길동")
			.birth("20000101")
			.gender(Gender.MALE)
			.area("서울")
			.introduction("자기소개입니다")
			.favorite(Favorite.GAME)
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));

		// when
		UserInfoResponse response = userService.readUserInfo(1L);

		// then
		assertEquals("홍길동", response.getName());
		assertEquals("20000101", response.getBirth());
		assertEquals("MALE", response.getGender().name());
		assertEquals("서울", response.getArea());
		assertEquals("자기소개입니다", response.getIntroduction());
		assertEquals("GAME", response.getFavorite().name());
	}

	@Test
	public void testUpdateUserInfo() {
		// given
		User mockUser = User.builder()
			.id(1L)
			.name("홍길동")
			.birth("20000101")
			.gender(Gender.MALE)
			.area("서울")
			.introduction("자기소개입니다")
			.favorite(Favorite.GAME)
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));
		when(userRepository.save(any(User.class)))
			.then(AdditionalAnswers.returnsFirstArg());

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

		// when
		UserInfoRequest request = UserInfoRequest.builder()
			.name("홈런볼")
			.birth("19991212")
			.gender(Gender.FEMALE)
			.area("인천")
			.introduction("레몬사탕")
			.build();
		userService.updateUserInfo(1L, request);

		// then
		verify(userRepository, times(1)).save(captor.capture());
		User capturedUser = captor.getValue();
		assertEquals("홈런볼", capturedUser.getName());
		assertEquals("19991212", capturedUser.getBirth());
		assertEquals(Gender.FEMALE, capturedUser.getGender());
		assertEquals("인천", capturedUser.getArea());
		assertEquals("레몬사탕", capturedUser.getIntroduction());
	}
}
