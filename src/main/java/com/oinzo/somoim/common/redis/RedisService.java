package com.oinzo.somoim.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;

	public void set(String key, Object o, long expiration) {
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
		redisTemplate.opsForValue().set(key, o, expiration, TimeUnit.SECONDS);
	}

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public Boolean delete(String key) {
		return redisTemplate.delete(key);
	}

	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	public void setBlackList(String key, Object o, int minutes) {
		redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
		redisBlackListTemplate.opsForValue().set(key, o, Duration.ofMinutes(minutes));
	}

	public Object getBlackList(String key) {
		return redisBlackListTemplate.opsForValue().get(key);
	}

	public Boolean deleteBlackList(String key) {
		return redisBlackListTemplate.delete(key);
	}

	public Boolean hasKeyBlackList(String key) {
		return redisBlackListTemplate.hasKey(key);
	}
}
