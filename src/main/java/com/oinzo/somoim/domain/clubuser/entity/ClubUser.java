package com.oinzo.somoim.domain.clubuser.entity;

import static javax.persistence.FetchType.LAZY;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.common.type.ClubUserLevel;
import com.oinzo.somoim.domain.club.entity.Club;
import com.oinzo.somoim.domain.user.entity.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ClubUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private ClubUserLevel level;

	@ManyToOne(fetch = LAZY)
	@JoinColumn
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn
	private Club club;

	public static ClubUser createClubUserManager(User user, Club club) {
		return ClubUser.builder()
			.level(ClubUserLevel.MANAGER)
			.user(user)
			.club(club)
			.build();
	}

	public static ClubUser createClubUserMember(User user, Club club) {
		return ClubUser.builder()
			.level(ClubUserLevel.MEMBER)
			.user(user)
			.club(club)
			.build();
	}
}
