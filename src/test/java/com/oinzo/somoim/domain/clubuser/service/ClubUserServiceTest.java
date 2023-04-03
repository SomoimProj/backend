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
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.controller.dto.MemberResponse;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubuser.entity.ClubUser;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import java.util.List;
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
			.description("게임 클럽입니다.")
			.area("서울")
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
			.description("게임 클럽입니다.")
			.area("서울")
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

	@Test
	void testGetMembers() {
		// given
		Club mockClub = Club.builder()
			.id(1L)
			.name("게임 클럽")
			.description("게임 클럽입니다.")
			.area("서울")
			.memberLimit(10)
			.memberCnt(2)
			.favorite(Favorite.GAME)
			.build();
		User mockUser1 = User.builder()
			.id(1L)
			.name("김김김")
			.profileUrl("url")
			.introduction("자기소개")
			.build();
		User mockUser2 = User.builder()
			.id(2L)
			.name("이이이")
			.build();
		List<ClubUser> clubUsers = List.of(
			new ClubUser(1L, ClubUserLevel.MEMBER, mockUser1, mockClub),
			new ClubUser(2L, ClubUserLevel.MEMBER, mockUser2, mockClub)
		);

		given(clubRepository.existsById(anyLong()))
			.willReturn(true);
		given(clubUserRepository.findByClub_Id(anyLong()))
			.willReturn(clubUsers);

		// when
		List<MemberResponse> members = clubUserService.getMembers(1L);

		// then
		assertEquals(2, members.size());
		assertEquals(1L, members.get(0).getUserId());
		assertEquals("김김김", members.get(0).getName());
		assertEquals("url", members.get(0).getProfileUrl());
		assertEquals("자기소개", members.get(0).getIntroduction());
		assertEquals(2L, members.get(1).getUserId());
		assertEquals("이이이", members.get(1).getName());
	}

	@Test
	void testGetMembers_wrongClubId() {
		given(clubRepository.existsById(anyLong()))
			.willReturn(false);

		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> clubUserService.getMembers(1L));

		// then
		assertEquals(ErrorCode.WRONG_CLUB, exception.getErrorCode());
	}

	@Test
	void testGetJoinClubs() {
		// given
		Club mockClub1 = Club.builder()
			.id(1L)
			.name("게임 클럽")
			.description("열정맨 게임 클럽입니다.")
			.area("서울")
			.memberLimit(10)
			.memberCnt(2)
			.favorite(Favorite.GAME)
			.build();
		Club mockClub2 = Club.builder()
			.id(2L)
			.name("스포츠 클럽")
			.description("스포츠 클럽입니다.")
			.area("인천")
			.memberLimit(10)
			.memberCnt(3)
			.favorite(Favorite.EXERCISE)
			.build();
		User mockUser = User.builder().id(1L).build();
		List<ClubUser> clubUsers = List.of(
			new ClubUser(1L, ClubUserLevel.MEMBER, mockUser, mockClub1),
			new ClubUser(2L, ClubUserLevel.MEMBER, mockUser, mockClub2)
		);

		given(clubUserRepository.findByUser_Id(anyLong()))
			.willReturn(clubUsers);

		// when
		List<ClubResponse> clubs = clubUserService.readJoinClubList(1L);

		// then
		assertEquals(2, clubs.size());
		assertEquals(1L, clubs.get(0).getId());
		assertEquals("게임 클럽", clubs.get(0).getName());
		assertEquals(Favorite.GAME, clubs.get(0).getFavorite());
		assertEquals(2L, clubs.get(1).getId());
		assertEquals("스포츠 클럽", clubs.get(1).getName());
		assertEquals(Favorite.EXERCISE, clubs.get(1).getFavorite());
	}
}
