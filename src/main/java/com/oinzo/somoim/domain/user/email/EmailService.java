package com.oinzo.somoim.domain.user.email;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.user.dto.EmailDto;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailService {

	@Value("${spring.mail.username}")
	private String sender;

	private final UserRepository userRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final JavaMailSender mailSender;
	private final String EMAIL_PREFIX = "email:";
	private final long VERIFICATION_CODE_EXPIRATION_TIME = 60 * 3L;
	private final long VERIFICATION_CODE_CHECK_SUCCESS = 60 * 60 * 24 * 3L;


	/**
	 * 인증코드 생성
	 */
	public String createCode() {

		Random random = new Random();
		StringBuilder code = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			int num = random.nextInt(3);

			switch (num) {
				case 0:
					code.append((char) (random.nextInt(26) + 97));
					break;
				case 1:
					code.append((char) (random.nextInt(26) + 65));
					break;
				case 2:
					code.append(random.nextInt(9));
					break;
			}
		}

		return code.toString();
	}

	/**
	 * 메일 내용 생성
	 */
	public MimeMessage createMailForm(String email, String code) throws MessagingException {

		String from = sender;
		String title = "소모임 회원가입을 위한 인증번호";

		MimeMessage message = mailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, email);
		message.setSubject(title);
		message.setFrom(from);
		message.setText(code, "utf-8", "html");

		return message;
	}

	/**
	 * 메일 전송
	 */
	public String sendVerificationCode(String email) {
		// 유저테이블에 이메일 존재여부 체크
		if (userRepository.findByEmail(email).isPresent()) {
			throw new BaseException(ErrorCode.ALREADY_EXISTS_EMAIL);
		}

		try {
			String verificationCode = createCode();
			MimeMessage mailForm = createMailForm(email, verificationCode);
			mailSender.send(mailForm);

			redisTemplate.opsForValue().set(
				EMAIL_PREFIX + email,
				verificationCode,
				Duration.ofSeconds(VERIFICATION_CODE_EXPIRATION_TIME)
			);

			return verificationCode;
		} catch (MessagingException e) {
			throw new BaseException(ErrorCode.FAILED_SEND_EMAIL, e.getMessage());
		}
	}

	/**
	 * 인증코드 검증
	 */
	public boolean checkVerificationCode(EmailDto emailDto) {

		if (redisTemplate.opsForValue().get(EMAIL_PREFIX + emailDto.getEmail()) == null) {
			throw new BaseException(ErrorCode.USER_NOT_FOUND);
		}

		String checkEmail = redisTemplate.opsForValue().get(EMAIL_PREFIX + emailDto.getEmail());

		if (!emailDto.getCode().equals(checkEmail)) {
			throw new BaseException(ErrorCode.WRONG_VERIFICATION_CODE);
		}

		redisTemplate.delete(EMAIL_PREFIX + emailDto.getEmail());
		redisTemplate.opsForValue().set(
			EMAIL_PREFIX + emailDto.getEmail(),
			emailDto.getCode(),
			Duration.ofSeconds(VERIFICATION_CODE_CHECK_SUCCESS)
		);

		return true;
	}
}
