package com.oinzo.somoim.domain.user.service;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.common.redis.RedisService;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

	@Value("${spring.mail.username}")
	private String sender;

	private final UserRepository userRepository;
	private final JavaMailSender mailSender;
	private final RedisService redisService;
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
		String text = "<h1>이메일 인증코드</h1>\n"
			+ "<p>소모임 회원가입을 위해 이메일 인증을 진행합니다.<br>아래의 인증코드를 입력하시면 이메일 인증이 완료됩니다.</p>\n"
			+ "<p style=\"background: #EFEFEF; font-size: 30px;padding: 10px\">" + code + "</p>";

		MimeMessage message = mailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, email);
		message.setSubject(title);
		message.setFrom(from);
		message.setText(text, "utf-8", "html");

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

			redisService.set(EMAIL_PREFIX + email,
				verificationCode,
				VERIFICATION_CODE_EXPIRATION_TIME);

			return verificationCode;
		} catch (MailException | MessagingException e) {
			log.error(e.getMessage(), e);
			throw new BaseException(ErrorCode.FAILED_SEND_EMAIL);
		}
	}

	/**
	 * 인증코드 검증
	 */
	public boolean checkVerificationCode(String email, String code) {
		String codeInRedis = (String) redisService.get(EMAIL_PREFIX + email);

		if (codeInRedis == null) {
			throw new BaseException(ErrorCode.USER_NOT_FOUND);
		}

		if (!code.equals(codeInRedis)) {
			return false;
		}

		redisService.delete(EMAIL_PREFIX + email);
		redisService.set(EMAIL_PREFIX + email,
			code,
			VERIFICATION_CODE_CHECK_SUCCESS);

		return true;
	}
}
