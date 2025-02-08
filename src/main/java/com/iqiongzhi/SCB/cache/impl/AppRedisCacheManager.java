package com.iqiongzhi.SCB.cache.impl;

import com.iqiongzhi.SCB.cache.IGlobalCache;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class AppRedisCacheManager implements IGlobalCache {

    private RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, String > hashOperations;

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
    public Map<String, Integer> hmgetAsIntegerMap(String key) {
        // 从 Redis 中获取原始数据
        Map<Object, Object> rawData = redisTemplate.opsForHash().entries(key);
        Map<String, Integer> result = new HashMap<>();

        for (Map.Entry<Object, Object> entry : rawData.entrySet()) {
            String k = entry.getKey().toString(); // 强制转换键为 String
            Integer v = null;

            try {
                // 将值转换为 Integer
                v = Integer.parseInt(entry.getValue().toString());
            } catch (NumberFormatException e) {
                System.err.println("无法将值 " + entry.getValue() + " 转换为 Integer，跳过该条记录。");
            }

            result.put(k, v);
        }

        return result;
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

    @Override
    public boolean zAdd(String key, Object value, double score) {
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, value, score));
    }

    @Override
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public boolean zRemove(String key, Object value) {
        Long removedCount = redisTemplate.opsForZSet().remove(key, value);
        return removedCount != null && removedCount > 0;
    }


    @Override
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    @Override
    public long zSize(String key) {
        Long size = redisTemplate.opsForZSet().size(key);
        return size != null ? size : 0;
    }

    @Override
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    @Override
    public Set<Object> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @Override
    public long zRemoveRangeByRank(String key, long maxRank) {
        return redisTemplate.opsForZSet().removeRange(key, maxRank, -1);
    }

}
