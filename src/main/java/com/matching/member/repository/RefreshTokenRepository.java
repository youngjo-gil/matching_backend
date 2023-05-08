package com.matching.member.repository;

import com.matching.member.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {
    private RedisTemplate redisTemplate;
    private RedisTemplate redisBlackListTemplate;
    private Long EXPIRE_TIME;

    public RefreshTokenRepository(
            RedisTemplate redisTemplate,
            @Value("${jwt.refresh-token-expire-time}") Long EXPIRE_TIME
    ) {
        this.redisTemplate = redisTemplate;
        this.redisBlackListTemplate = redisTemplate;
        this.EXPIRE_TIME = EXPIRE_TIME;
    }

    public void save(RefreshToken refreshToken) {
        ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(refreshToken.getUserId(), refreshToken.getRefreshToken(), EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    public void delete(RefreshToken refreshToken) {
        if(redisTemplate.opsForValue().get(refreshToken.getUserId()) != null) {
            redisTemplate.delete(refreshToken.getUserId());
        }
    }

    public void updateBlackList(String key, Long milliSeconds) {
        redisBlackListTemplate.opsForValue().set(key, "logout", milliSeconds, TimeUnit.MILLISECONDS);
    }

    public boolean hasKeyBlackList(String key) {
        return redisBlackListTemplate.hasKey(key);
    }

    public Optional<RefreshToken> findById(Long userId) {
        ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();

        String token = valueOperations.get(userId);

        if(ObjectUtils.isEmpty(token)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(userId, token));
    }
}
