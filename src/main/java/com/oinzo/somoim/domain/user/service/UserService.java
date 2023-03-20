package com.oinzo.somoim.domain.user.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    public void signUp(String email, String password, String passwordCheck) {

        if (userRepository.existsByEmail(email)) {
            throw new BaseException(ErrorCode.ALREADY_EXISTS_EMAIL);
        }

        if (!password.equals(passwordCheck)) {
            throw new BaseException(ErrorCode.WRONG_PASSWORD);
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncode(password))
                .build();

        userRepository.save(user);
    }
}
