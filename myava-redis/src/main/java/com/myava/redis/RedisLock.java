package com.myava.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis分布式锁，基于StringRedisTemplate操作
 *
 * @author biao
 */
@Slf4j
public class RedisLock {

    private StringRedisTemplate redisTemplate;
    /**
     * 重试时间，单位毫秒
     */
    private static final int DEFAULT_RETRY_TIME_MILLIS = 100;
    /**
     * 锁的前缀
     */
    private static final String LOCK_PREFIX = "redis:lock:";
    /**
     * 锁的key
     */
    private String lockKey;
    /**
     * 锁超时时间，防止线程在入锁以后，防止阻塞后面的线程无法获取锁
     */
    private int expireMsecs = 60 * 1000;
    /**
     * 线程获取锁的等待时间
     */
    private int timeoutMsecs = 10 * 1000;
    /**
     * 是否锁定标志
     */
    private volatile boolean locked = false;

    public RedisLock(StringRedisTemplate redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = LOCK_PREFIX + lockKey;
    }

    public RedisLock(StringRedisTemplate redisTemplate, String lockKey, int timeoutMsecs) {
        this(redisTemplate, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    public RedisLock(StringRedisTemplate redisTemplate, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(redisTemplate, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    public RedisLock(String lockKey) {
        this(SpringContext.getBean(StringRedisTemplate.class), lockKey);
    }

    public RedisLock(String lockKey, int timeoutMsecs) {
        this(SpringContext.getBean(StringRedisTemplate.class), lockKey, timeoutMsecs);
    }

    public String getLockKey() {
        return lockKey;
    }

    private String get(final String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        return obj != null ? obj.toString() : null;
    }

    private boolean setNX(final String key, final String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    private String getSet(final String key, final String value) {
        Object obj = redisTemplate.opsForValue().getAndSet(key, value);
        return obj != null ? (String) obj : null;
    }

    /**
     * 获取锁
     * @return
     */
    public synchronized boolean lock() {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            // 锁到期时间
            String expiresStr = String.valueOf(expires);
            if (this.setNX(lockKey, expiresStr)) {
                locked = true;
                return true;
            }
            // redis里key的时间
            String currValue = this.get(lockKey);
            // 判断锁是否已经过期，过期则重新设置并获取
            if (currValue != null && Long.parseLong(currValue) < System.currentTimeMillis()) {
                // 设置锁并返回旧值
                String oldValue = this.getSet(lockKey, expiresStr);
                // 比较锁的时间，如果不一致则可能是其他锁已经修改了值并获取
                if (oldValue != null && oldValue.equals(currValue)) {
                    locked = true;
                    return true;
                }
            }
            timeout -= DEFAULT_RETRY_TIME_MILLIS;
            try {
                Thread.sleep(DEFAULT_RETRY_TIME_MILLIS);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }

        }
        return false;
    }

    /**
     * 释放获取到的锁
     */
    public synchronized void unlock() {
        if (locked) {
            redisTemplate.delete(lockKey);
            locked = false;
        }
    }

}
