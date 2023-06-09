package com.oinzo.somoim.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.controller.dto.UserInfoRequest;
import com.oinzo.somoim.controller.dto.UserInfoResponse;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
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
			.birth(LocalDate.parse("2000-01-01"))
			.gender(Gender.MALE)
			.area("서울")
			.introduction("자기소개입니다")
			.favorites(List.of(Favorite.GAME, Favorite.PICTURE))
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));

		// when
		UserInfoResponse response = userService.readUserInfo(1L);

		// then
		assertEquals(1L, response.getId());
		assertEquals("홍길동", response.getName());
		assertEquals("2000-01-01", response.getBirth().toString());
		assertEquals("MALE", response.getGender().name());
		assertEquals("서울", response.getArea());
		assertEquals("자기소개입니다", response.getIntroduction());
		assertEquals(2, response.getFavorites().size());
		assertTrue(response.getFavorites().contains(Favorite.GAME));
		assertTrue(response.getFavorites().contains(Favorite.PICTURE));
	}

	@Test
	public void testUpdateUserInfo() {
		// given
		User mockUser = User.builder()
			.id(1L)
			.name("홍길동")
			.birth(LocalDate.parse("2000-01-01"))
			.gender(Gender.MALE)
			.area("서울")
			.introduction("자기소개입니다")
			.favorites(List.of(Favorite.GAME, Favorite.PICTURE))
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));
		when(userRepository.save(any(User.class)))
			.then(AdditionalAnswers.returnsFirstArg());

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

		// when
		UserInfoRequest request = UserInfoRequest.builder()
			.name("홈런볼")
			.birth("1999-12-12")
			.gender(Gender.FEMALE)
			.area("인천")
			.introduction("레몬사탕")
			.build();
		userService.updateUserInfo(1L, request);

		// then
		verify(userRepository, times(1)).save(captor.capture());
		User capturedUser = captor.getValue();
		assertEquals("홈런볼", capturedUser.getName());
		assertEquals("1999-12-12", capturedUser.getBirth().toString());
		assertEquals(Gender.FEMALE, capturedUser.getGender());
		assertEquals("인천", capturedUser.getArea());
		assertEquals("레몬사탕", capturedUser.getIntroduction());
	}

	@Test
	public void testUpdateFavorite() {
		// given
		User mockUser = User.builder()
			.id(1L)
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

		// when
		userService.updateFavorite(1L, List.of("GAME", "PICTURE"));

		// then
		verify(userRepository, times(1)).save(captor.capture());
		List<Favorite> favorites = captor.getValue().getFavorites();
		assertEquals(2, favorites.size());
		assertTrue(favorites.contains(Favorite.GAME));
		assertTrue(favorites.contains(Favorite.PICTURE));
	}

	@Test
	public void testUpdateFavorite_wrongFavorite() {
		// given
		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> userService.updateFavorite(1L, List.of("WRONG_FAVORITE")));

		// then
		assertEquals(ErrorCode.WRONG_FAVORITE, exception.getErrorCode());
	}
}
