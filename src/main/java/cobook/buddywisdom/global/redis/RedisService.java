package cobook.buddywisdom.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(Long key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(String.valueOf(key), data);
        log.info("Redis Saved : " + redisTemplate.opsForValue().get(String.valueOf(key)));
    }

    public String getValues(Long key) {
        log.info("Redis Get : " + redisTemplate.opsForValue().get(String.valueOf(key)));
        return redisTemplate.opsForValue().get(String.valueOf(key));
    }

    public void deleteValues(Long key) {
        log.info("Redis delete : " + redisTemplate.opsForValue().get(String.valueOf(key)));
        redisTemplate.delete(String.valueOf(key));
    }

    public boolean isExists(Long key) {
        return redisTemplate.hasKey(String.valueOf(key));
    }
}
