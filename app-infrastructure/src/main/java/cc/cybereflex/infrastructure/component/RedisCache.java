package cc.cybereflex.infrastructure.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class RedisCache {

    private final StringRedisTemplate redisTemplate;


    public boolean lock(String key, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "lock", timeout, timeUnit));
    }

    public boolean spinLock(String key, long timeout, TimeUnit timeUnit, int retryCount) {
        try {
            int count = 0;
            while (true) {
                if (count > retryCount) {
                    return false;
                }

                if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "lock", timeout, timeUnit))) {
                    return true;
                }

                count++;

                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean unlock(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Long delete(String... key) {
        return redisTemplate.delete(Arrays.asList(key));
    }

    public void setList(String key, List<String> data) {
        redisTemplate.opsForList().rightPushAll(key, data);
    }

    public List<String> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public void setSet(String key, Set<String> data) {
        redisTemplate.opsForSet().add(key, data.toArray(String[]::new));
    }

    public Set<String> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void setMap(String key, Map<String, String> data) {
        redisTemplate.opsForHash().putAll(key, data);
    }

    public Map<String, String> getMap(String key) {
        return redisTemplate.<String, String>opsForHash().entries(key);
    }

    public void setMapValue(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public String getMapValue(String key, String hashKey) {
        return redisTemplate.<String, String>opsForHash().get(key, hashKey);
    }

    public List<String> getMultiMapValue(String key, Collection<String> hashKeys) {
        return redisTemplate.<String, String>opsForHash().multiGet(key, hashKeys);
    }

    public Long deleteMapValue(String key, String hashKey) {
        return redisTemplate.opsForHash().delete(key, hashKey);
    }
}
