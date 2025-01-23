package com.iqiongzhi.SCB.cache.impl;

import com.iqiongzhi.SCB.cache.IGlobalCache;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class AppRedisCacheManager implements IGlobalCache {

    private RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> hashOperations;

    @Override
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    @Override
    public boolean set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
        return true;
    }

    @Override
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    @Override
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
        return true;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

    @Override
    public boolean hset(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
        return true;
    }

    @Override
    public boolean hset(String key, String item, Object value, long time) {
        redisTemplate.opsForHash().put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

    @Override
    public Set<String> getAllKeysFromHash(String hashKey) {
        return hashOperations.keys(hashKey);
    }

    @Override
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    @Override
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    @Override
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    @Override
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    @Override
    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public long sSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        if (time > 0) {
            expire(key, time);
        }
        return count;
    }

    @Override
    public long sGetSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public long lGetListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Object lGetIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public boolean lSetAll(String key, List<Object> value) {
        redisTemplate.opsForList().leftPushAll(key, value);
        return true;
    }

    @Override
    public boolean lSet(String key, Object value) {
        redisTemplate.opsForList().leftPushIfPresent(key, value);
        return true;
    }

    @Override
    public boolean lSet(String key, Object value, long time) {
        redisTemplate.opsForList().leftPush(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;

    }

    @Override
    public boolean lSetAll(String key, List<Object> value, long time) {
        redisTemplate.opsForList().leftPushAll(key, value);
        if (time > 0)
            expire(key, time);
        return true;
    }

    @Override
    public boolean rSet(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
        return true;
    }

    @Override
    public boolean rSet(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;

    }

    @Override
    public boolean rSetAll(String key, List<Object> value) {
        redisTemplate.opsForList().rightPushAll(key, value);
        return true;

    }

    @Override
    public boolean rSetAll(String key, List<Object> value, long time) {
        redisTemplate.opsForList().rightPushAll(key, value);
        if (time > 0)
            expire(key, time);
        return true;
    }

    @Override
    public boolean lUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
        return true;
    }

    @Override
    public long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public void rangeRemove(String key, Long stard, Long end) {
        redisTemplate.opsForList().trim(key, stard, end);
    }
}
