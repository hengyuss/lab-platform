package com.hengyu.lab.framework.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisCache {

  private final RedisTemplate<Object, Object> redisTemplate;

  public <T> void setCacheObject(final String key, final T value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public <T> void setCacheObject(final String key, final T value, final Integer expireTime,
      TimeUnit timeUnit) {
    redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
  }

  public boolean expire(final String key, final Integer expireTime,  final TimeUnit timeUnit) {
    return Boolean.TRUE.equals(redisTemplate.expire(key, expireTime, timeUnit));
  }

  public boolean hasKey(final String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public <T> T getCacheObject(final String key) {
    return (T) redisTemplate.opsForValue().get(key);
  }

  public boolean deleteObject(final String key) {
    return Boolean.TRUE.equals(redisTemplate.delete(key));
  }


}
