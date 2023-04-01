package com.oinzo.somoim.domain.clublike.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.club.repository.ClubRepository;
import com.oinzo.somoim.domain.clublike.entity.ClubLike;
import com.oinzo.somoim.domain.clublike.repository.ClubLikeRepository;
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

}
