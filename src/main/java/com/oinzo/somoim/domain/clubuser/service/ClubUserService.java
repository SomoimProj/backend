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
		if (club.getMemberLimit() == readMembersCount(clubId)) {
			throw new BaseException(ErrorCode.CLUB_LIMIT_OVER,
				"clubId=" + clubId + ", memberLimit=" + club.getMemberLimit());
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
		List<ClubUser> clubUserList = clubUserRepository.findByClub_Id(clubId);
		return clubUserList.stream()
			.map(clubUser -> MemberResponse.from(clubUser.getUser()))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ClubResponse> readJoinClubList(Long userId) {
		// userId는 JWT 토큰으로부터 인증된 값이므로 DB에 있는지 조사할 필요 없음.
		List<ClubUser> clubUserList = clubUserRepository.findByUser_Id(userId);
		return clubUserList.stream()
			.map(clubUser -> {
				Club club = clubUser.getClub();
				Long memberCnt = readMembersCount(club.getId());
				return ClubResponse.fromClubAndMemberCnt(club, memberCnt);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Long readClubManagerId(long clubId) {
		ClubUser managerClubUser = clubUserRepository.findByClub_IdAndLevel(clubId, ClubUserLevel.MANAGER)
			.orElseThrow(() -> new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, "클럽의 매니저 정보가 조회되지 않습니다. clubId=" + clubId));
		return managerClubUser.getUser().getId();
	}

	public Long readMembersCount(Long clubId) {
		if (!clubRepository.existsById(clubId)) {
			throw new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId);
		}
		return clubUserRepository.countByClub_Id(clubId);
	}
}
