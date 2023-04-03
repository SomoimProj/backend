package com.oinzo.somoim.domain.clublike.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.controller.dto.ClubResponse;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clublike.entity.ClubLike;
import com.oinzo.somoim.domain.clublike.repository.ClubLikeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClubLikeService {

	private final ClubLikeRepository clubLikeRepository;
	private final ClubRepository clubRepository;

	@Transactional
	public void addLike(long userId, long clubId) {
		// userId는 JWT 토큰으로부터 인증된 값이므로 DB에 있는지 조사할 필요 없음.
		Club club = clubRepository.findById(clubId)
			.orElseThrow(() -> new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId));
		if (clubLikeRepository.existsByClub_IdAndUserId(clubId, userId)) {
			throw new BaseException(ErrorCode.ALREADY_LIKED, "clubId=" + clubId);
		}
		ClubLike clubLike = ClubLike.builder()
			.club(club)
			.userId(userId)
			.build();
		clubLikeRepository.save(clubLike);
	}

	@Transactional
	public void deleteLike(long userId, long clubId) {
		// userId는 JWT 토큰으로부터 인증된 값이므로 DB에 있는지 조사할 필요 없음.
		if (!clubRepository.existsById(clubId)) {
			throw new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId);
		}
		ClubLike clubLike = clubLikeRepository.findByClub_IdAndUserId(clubId, userId)
			.orElseThrow(() -> new BaseException(ErrorCode.WRONG_LIKE, "clubId=" + clubId));
		clubLikeRepository.delete(clubLike);
	}
	
	@Transactional(readOnly = true)
	public List<ClubResponse> readLikeClubList(long userId) {
		// userId는 JWT 토큰으로부터 인증된 값이므로 DB에 있는지 조사할 필요 없음.
		List<ClubLike> clubLikeList = clubLikeRepository.findAllByUserIdOrderByIdDesc(userId);
		return clubLikeList.stream()
			.map(clubLike -> ClubResponse.from(clubLike.getClub()))
			.collect(Collectors.toList());
	}

	public Long readLikesCount(Long clubId) {
		if (!clubRepository.existsById(clubId)) {
			throw new BaseException(ErrorCode.WRONG_CLUB, "clubId=" + clubId);
		}
		return clubLikeRepository.countByClub_Id(clubId);
	}

}
