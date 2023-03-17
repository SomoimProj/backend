package com.oinzo.somoim.domain.user.entity;

import com.oinzo.somoim.common.entity.BaseEntity;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.common.type.SocialType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String birth;
	@Enumerated(value = EnumType.STRING)
	private Gender gender;
	private String area;
	private String introduction;
	private String profileUrl;
	private String favorite;

	private String email;
	private String password;

	@Enumerated(value = EnumType.STRING)
	private SocialType socialType;
	private String socialId;
}
