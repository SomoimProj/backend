package com.oinzo.somoim.controller.dto;

import com.oinzo.somoim.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam String email, @RequestParam String password, @RequestParam String passwordCheck) {

        userService.signUp(email, password, passwordCheck);

        return ResponseEntity.ok().build();
    }
}
