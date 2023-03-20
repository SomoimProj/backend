package com.oinzo.somoim.domain.user.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@RedisHash(value = "email")
public class Email {

    @Id
    private String email;

    private String code;

    @TimeToLive
    private Long expiredTime;

    public Email(String email, String code, long expiredTime) {
        this.email = email;
        this.code = code;
        this.expiredTime = expiredTime;
    }
}