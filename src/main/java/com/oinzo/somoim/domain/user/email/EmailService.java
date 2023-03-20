package com.oinzo.somoim.domain.user.email;

import com.oinzo.somoim.common.exception.BaseException;
import com.oinzo.somoim.common.exception.ErrorCode;
import com.oinzo.somoim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;


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
    public String sendMail(String email) throws MessagingException {
        // 유저테이블에 이메일 존재여부 체크
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_EXISTS_EMAIL, ErrorCode.ALREADY_EXISTS_EMAIL.getMessage());
        }

        String verificationCode = createCode();
        MimeMessage mailForm = createMailForm(email, verificationCode);
        mailSender.send(mailForm);

        // mapper
        Email newEmail = new Email(email, verificationCode, 60 * 3);

        // 레디스에 3분동안 저장
        try {
            Email checkEmail = emailRepository.findById(email).get();
            emailRepository.delete(checkEmail);
            emailRepository.save(newEmail);
        } catch (Exception e) {
            emailRepository.save(newEmail);
        }
        return verificationCode;
    }

    /**
     * 인증코드 검증
     */
    public Object checkVerificationCode(String email, String code) {

        Email checkEmail = emailRepository.findById(email).get();

        if (!checkEmail.getCode().equals(code)) {
            throw new BaseException(ErrorCode.WRONG_VERIFICATION_CODE, ErrorCode.WRONG_VERIFICATION_CODE.getMessage());
        }

        emailRepository.delete(checkEmail);
        // 인증 성공 시 데이터를 3일간 저장
        emailRepository.save(checkEmail);

        return checkEmail;
    }
}