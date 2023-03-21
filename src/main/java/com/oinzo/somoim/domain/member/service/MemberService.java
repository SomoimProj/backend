package com.oinzo.somoim.domain.member.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.member.entity.Member;
import com.oinzo.somoim.domain.member.repository.MemberRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	private final UserRepository userRepository;
	private final ClubRepository clubRepository;

	@Transactional
	public void joinClub(Long userId, Long clubId, String introduction) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));
		Club club = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId));

		// 이미 가입된 회원인지 확인
		if (memberRepository.existsByUserAndClub(user, club)) {
			throw new BaseException(ErrorCode.ALREADY_CLUB_MEMBER,
				"userId=" + userId + ", clubId" + clubId);
		}

		// 멤버 정원이 꽉 찼는지 확인
		if (club.getMemberLimit() == club.getMemberCnt()) {
			throw new BaseException(ErrorCode.CLUB_LIMIT_OVER,
				"clubId=" + club.getId() + ", memberLimit=" + club.getMemberLimit());
		}

		// 멤버로 등록
		Member member = Member.createMember(user, club, introduction);
		memberRepository.save(member);
	}
}
