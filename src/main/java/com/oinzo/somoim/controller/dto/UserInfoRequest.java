package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.common.annotation.Birth;
import com.oinzo.somoim.common.type.Favorite;
import com.oinzo.somoim.common.type.Gender;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRequest {
	@NotBlank
	private String name;
	@Birth
	private String birth;
	private Gender gender;
	@NotBlank
	private String area;
	private String introduction;
	@URL
	private String profileUrl;
}
