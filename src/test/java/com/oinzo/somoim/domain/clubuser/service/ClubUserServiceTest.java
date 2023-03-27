package com.oinzo.somoim.domain.clubuser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.ClubUserLevel;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubuser.entity.ClubUser;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClubUserServiceTest {

	@InjectMocks
	private ClubUserService clubUserService;

	@Mock
	private ClubUserRepository clubUserRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ClubRepository clubRepository;

	@Test
	void testJoinClub() {
		// given
		User mockUser = User.builder()
			.id(1L)
			.build();
		Club mockClub = Club.builder()
			.id(1L)
			.name("게임 클럽")
			.description("열정맨 게임 클럽입니다.")
			.area("청파동")
			.memberLimit(4)
			.memberCnt(0)
			.favorite(Favorite.GAME)
			.build();
		given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(), anyLong()))
			.willReturn(false);
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.of(mockClub));
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));

		ArgumentCaptor<ClubUser> captor = ArgumentCaptor.forClass(ClubUser.class);

		// when
		clubUserService.joinClub(1L, 1L);

		// then
		verify(clubUserRepository, times(1)).save(captor.capture());
		assertEquals(1L, captor.getValue().getUser().getId());
		assertEquals(1L, captor.getValue().getClub().getId());
		assertEquals(1, captor.getValue().getClub().getMemberCnt());
		assertEquals(ClubUserLevel.MEMBER, captor.getValue().getLevel());
	}

	@Test
	void testJoinClub_memberLimitOver() {
		// given
		Club mockClub = Club.builder()
			.id(1L)
			.name("게임 클럽")
			.description("열정맨 게임 클럽입니다.")
			.area("청파동")
			.memberLimit(1)
			.memberCnt(1)
			.favorite(Favorite.GAME)
			.build();
		given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(), anyLong()))
			.willReturn(false);
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.of(mockClub));

		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> clubUserService.joinClub(1L, 1L));

		// then
		assertEquals(ErrorCode.CLUB_LIMIT_OVER, exception.getErrorCode());
	}

	@Test
	void testJoinClub_alreadyMember() {
		// given
		given(clubUserRepository.existsByUser_IdAndClub_Id(anyLong(), anyLong()))
			.willReturn(true);

		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> clubUserService.joinClub(1L, 1L));

		// then
		assertEquals(ErrorCode.ALREADY_CLUB_MEMBER, exception.getErrorCode());
	}
}
