package com.oinzo.somoim.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.MemberLevel;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.member.entity.Member;
import com.oinzo.somoim.domain.member.repository.MemberRepository;
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
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

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
			.name("스포츠 클럽")
			.description("열정맨 스포츠 클럽입니다.")
			.area("청파동")
			.memberLimit(4)
			.memberCnt(0)
			.favorite(Favorite.SPORTS)
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.of(mockClub));
		given(memberRepository.existsByUserAndClub(any(), any()))
			.willReturn(false);

		ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

		// when
		String introduction = "안녕하세요 스포츠 클럽에 가입합니다.";
		memberService.joinClub(1L, 1L, introduction);

		// then
		verify(memberRepository, times(1)).save(captor.capture());
		assertEquals(1L, captor.getValue().getUser().getId());
		assertEquals(1L, captor.getValue().getClub().getId());
		assertEquals(1, captor.getValue().getClub().getMemberCnt());
		assertEquals(introduction, captor.getValue().getIntroduction());
		assertEquals(MemberLevel.MEMBER, captor.getValue().getLevel());
	}

	@Test
	void testJoinClub_memberLimitOver() {
		// given
		User mockUser = User.builder()
			.id(1L)
			.build();
		Club mockClub = Club.builder()
			.id(1L)
			.name("스포츠 클럽")
			.description("열정맨 스포츠 클럽입니다.")
			.area("청파동")
			.memberLimit(1)
			.memberCnt(1)
			.favorite(Favorite.SPORTS)
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.of(mockClub));
		given(memberRepository.existsByUserAndClub(any(), any()))
			.willReturn(false);

		// when
		String introduction = "안녕하세요 스포츠 클럽에 가입합니다.";
		BaseException exception = assertThrows(BaseException.class,
			() -> memberService.joinClub(1L, 1L, introduction));

		// then
		assertEquals(ErrorCode.CLUB_LIMIT_OVER, exception.getErrorCode());
	}

	@Test
	void testJoinClub_alreadyMember() {
		// given
		User mockUser = User.builder()
			.id(1L)
			.build();
		Club mockClub = Club.builder()
			.id(1L)
			.name("스포츠 클럽")
			.description("열정맨 스포츠 클럽입니다.")
			.area("청파동")
			.memberLimit(5)
			.memberCnt(1)
			.favorite(Favorite.SPORTS)
			.build();
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(mockUser));
		given(clubRepository.findById(anyLong()))
			.willReturn(Optional.of(mockClub));
		given(memberRepository.existsByUserAndClub(any(), any()))
			.willReturn(true);

		// when
		String introduction = "안녕하세요 스포츠 클럽에 가입합니다.";
		BaseException exception = assertThrows(BaseException.class,
			() -> memberService.joinClub(1L, 1L, introduction));

		// then
		assertEquals(ErrorCode.ALREADY_CLUB_MEMBER, exception.getErrorCode());
	}
}
