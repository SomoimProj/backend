package com.oinzo.somoim.domain.user.email;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RequestMapping("/email")
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestParam String email) throws MessagingException {
        String code = emailService.sendMail(email);

        return ResponseEntity.ok(code);
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkCode(@RequestParam String email, @RequestParam String code) {
        emailService.checkVerificationCode(email, code);

        return ResponseEntity.ok().build();
    }
}