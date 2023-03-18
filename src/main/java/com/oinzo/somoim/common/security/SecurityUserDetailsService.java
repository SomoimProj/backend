package com.oinzo.somoim.common.security;

import static com.oinzo.somoim.common.exception.ErrorCode.USER_NOT_FOUND;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) {
		User user = userRepository.findById(Long.parseLong(userId))
			.orElseThrow(() -> new BaseException(USER_NOT_FOUND, "userId=" + userId));

		return new SecurityUser(user);
	}

}
