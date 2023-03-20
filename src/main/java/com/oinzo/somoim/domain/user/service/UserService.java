package com.oinzo.somoim.domain.user.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.type.Gender;
import com.oinzo.somoim.domain.user.entity.User;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }

    public void signUp(String email, String password, String passwordCheck) {

        boolean exists = userRepository.findByEmail(email).isPresent();

        if (exists) {
            throw new BaseException(ErrorCode.ALREADY_EXISTS_EMAIL, ErrorCode.ALREADY_EXISTS_EMAIL.getMessage());
        }

        if (!password.equals(passwordCheck))
            throw new BaseException(ErrorCode.WRONG_PASSWORD, ErrorCode.WRONG_PASSWORD.getMessage());

        User user = User.builder()
                .email(email)
                .password(passwordEncode(password))
                .gender(Gender.MALE)
                .build();

        userRepository.save(user);
    }
}
