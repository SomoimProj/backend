package com.oinzo.somoim.domain.clubuser.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.controller.dto.MemberResponse;
import com.oinzo.somoim.common.type.ClubUserLevel;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clubuser.entity.ClubUser;
import com.oinzo.somoim.domain.clubuser.repository.ClubUserRepository;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClubUserService {

	private final ClubUserRepository clubUserRepository;

	private final UserRepository userRepository;
	private final ClubRepository clubRepository;

	@Transactional
	public void joinClub(Long userId, Long clubId) {
		// 이미 가입된 회원인지 확인
		if (clubUserRepository.existsByUser_IdAndClub_Id(userId, clubId)) {
			throw new BaseException(ErrorCode.ALREADY_CLUB_MEMBER,
				"userId=" + userId + ", clubId=" + clubId);
		}

		Club club = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId));

		// 멤버 정원이 꽉 찼는지 확인
		if (club.getMemberLimit() == club.getMemberCnt()) {
			throw new BaseException(ErrorCode.CLUB_LIMIT_OVER,
				"clubId=" + club.getId() + ", memberLimit=" + club.getMemberLimit());
		}

		// 클럽 멤버로 등록
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "userId=" + userId));
		ClubUser clubUser = ClubUser.createClubUserMember(user, club);
		clubUserRepository.save(clubUser);
	}

	@Transactional(readOnly = true)
	public List<MemberResponse> getMembers(Long clubId) {
		if (!clubRepository.existsById(clubId)) {
			throw new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId);
		}
		List<ClubUser> clubUsers = clubUserRepository.findByClub_Id(clubId);
		return clubUsers.stream()
			.map(clubUser -> MemberResponse.from(clubUser.getUser()))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ClubResponse> getJoinClubs(Long userId) {
		// userId는 JWT 토큰으로부터 인증된 값이므로 DB에 있는지 조사할 필요 없음.
		List<ClubUser> clubUsers = clubUserRepository.findByUser_Id(userId);
		return clubUsers.stream()
			.map(clubUser -> ClubResponse.from(clubUser.getClub()))
			.collect(Collectors.toList());

	public Long getClubManagerId(long clubId) {
		ClubUser managerClubUser = clubUserRepository.findByClub_IdAndLevel(clubId, ClubUserLevel.MANAGER);
		return managerClubUser.getUser().getId();
	}
}
