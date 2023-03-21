package com.oinzo.somoim.domain.member.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.common.type.MemberLevel;
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
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private MemberLevel level;
	private String introduction;

	@ManyToOne
	@JoinColumn
	private User user;

	@ManyToOne
	@JoinColumn
	private Club club;

}
