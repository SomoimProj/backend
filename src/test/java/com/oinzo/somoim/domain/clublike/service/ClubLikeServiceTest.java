package com.oinzo.somoim.domain.clublike.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clublike.entity.ClubLike;
import com.oinzo.somoim.domain.clublike.repository.ClubLikeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClubLikeServiceTest {

	@InjectMocks
	private ClubLikeService clubLikeService;

	@Mock
	private ClubLikeRepository clubLikeRepository;

	@Mock
	private ClubRepository clubRepository;

	@Test
	void testAddLike() {
		// given
		Club mockClub = Club.builder()
			.id(1L)
			.name("게임 클럽")
			.description("게임 클럽입니다.")
			.area("서울")
			.memberLimit(4)
			.memberCnt(0)
			.favorite(Favorite.GAME)
			.build();
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.of(mockClub));
		given(clubLikeRepository.existsByClub_IdAndUserId(anyLong(), anyLong()))
			.willReturn(false);

		ArgumentCaptor<ClubLike> captor = ArgumentCaptor.forClass(ClubLike.class);

		// when
		clubLikeService.addLike(1L, 1L);

		// then
		verify(clubLikeRepository, times(1)).save(captor.capture());
		assertEquals(1L, captor.getValue().getClub().getId());
		assertEquals("게임 클럽", captor.getValue().getClub().getName());
		assertEquals(1L, captor.getValue().getUserId());
	}

	@Test
	void testAddLike_wrongClub() {
		// given
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> clubLikeService.addLike(1L, 1L));

		// then
		assertEquals(ErrorCode.WRONG_CLUB, exception.getErrorCode());
	}

	@Test
	void testAddLike_AlreadyLikedClub() {
		// given
		Club mockClub = Club.builder()
			.id(1L)
			.name("게임 클럽")
			.description("게임 클럽입니다.")
			.area("서울")
			.memberLimit(4)
			.memberCnt(0)
			.favorite(Favorite.GAME)
			.build();
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.of(mockClub));
		given(clubLikeRepository.existsByClub_IdAndUserId(anyLong(), anyLong()))
			.willReturn(true);

		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> clubLikeService.addLike(1L, 1L));

		// then
		assertEquals(ErrorCode.ALREADY_LIKED, exception.getErrorCode());
	}

	@Test
	void testDeleteLike() {
		// given
		Club mockClub = Club.builder()
			.id(1L)
			.name("게임 클럽")
			.description("게임 클럽입니다.")
			.area("서울")
			.memberLimit(4)
			.memberCnt(0)
			.favorite(Favorite.GAME)
			.build();
		ClubLike mockClubLike = ClubLike.builder()
			.id(1L)
			.club(mockClub)
			.userId(1L)
			.build();
		given(clubRepository.existsById(anyLong()))
			.willReturn(true);
		given(clubLikeRepository.findByClub_IdAndUserId(anyLong(), anyLong()))
			.willReturn(Optional.of(mockClubLike));

		ArgumentCaptor<ClubLike> captor = ArgumentCaptor.forClass(ClubLike.class);

		// when
		clubLikeService.deleteLike(1L, 1L);

		// then
		verify(clubLikeRepository, times(1)).delete(captor.capture());
		assertEquals(1L, captor.getValue().getClub().getId());
		assertEquals("게임 클럽", captor.getValue().getClub().getName());
		assertEquals(1L, captor.getValue().getUserId());
	}

	@Test
	void testDeleteLike_wrongClub() {
		// given
		given(clubRepository.existsById(anyLong()))
			.willReturn(false);

		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> clubLikeService.deleteLike(1L, 1L));

		// then
		assertEquals(ErrorCode.WRONG_CLUB, exception.getErrorCode());
	}

	@Test
	void testDeleteLike_wrongLike() {
		// given
		given(clubRepository.existsById(anyLong()))
			.willReturn(true);
		given(clubLikeRepository.findByClub_IdAndUserId(anyLong(), anyLong()))
			.willReturn(Optional.empty());

		// when
		BaseException exception = assertThrows(BaseException.class,
			() -> clubLikeService.deleteLike(1L, 1L));

		// then
		assertEquals(ErrorCode.WRONG_LIKE, exception.getErrorCode());
	}

}
