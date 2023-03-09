package com.gdsc.bakku.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    public void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }
    public Double ZsetGetScore(String key, String value) {
        return zSetOperations.score(key, value);
    }

    public void ZSetAdd(String key, String value, Double score) {
        zSetOperations.add(key, value, score);
    }

    public void ZSetDelete(String key, String value) {
        zSetOperations.remove(key, value);
    }

    public void ZsetAddOrUpdate(String key, String value, Double score) {
        Double presentWeight = ZsetGetScore(key, value);

        if (presentWeight == null) {
            ZSetAdd(key, value, score);
        } else {
            Double totalScore = presentWeight + score;
            if (isZeroOrLess(totalScore)) {
                ZSetDelete(key, value);
            } else {
                ZSetAdd(key, value, presentWeight + score);
            }
        }
    }

    public Set<String> getZset(String key, long end) {
        return zSetOperations.reverseRange(key, 0, end);
    }

    private boolean isZeroOrLess(Double value) {
        return value <= 0.0;
    }
}
