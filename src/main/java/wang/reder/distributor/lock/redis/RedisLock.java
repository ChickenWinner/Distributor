package wang.reder.distributor.lock.redis;

import redis.clients.jedis.Jedis;
import wang.reder.distributor.lock.IDtorLock;
import wang.reder.distributor.utils.IdUtils;
import wang.reder.distributor.utils.redis.AbstractJedis;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>基于Redis实现的分布式锁<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/1 11:03
 */
public class RedisLock extends AbstractJedis implements IDtorLock {

    // Redis命令常用字符串
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    // 默认锁过期时间 10s
    private int lockTimeout = 10000;

    // 重试时间 0.5s
    private int retryAwait = 500;

    // new一个锁，指定名字，锁过期时间
    public RedisLock(String lockName, int lockTimeout) {
        this.lockTimeout = lockTimeout;
        setKey(lockName);
    }

    // new一个锁，指定名字
    public RedisLock(String lockName) {
        setKey(lockName);
    }


    // 加锁直到成功为止
    @Override
    public String lock() {
        return this.tryLock(-1, null);
    }

    // 尝试加锁，2s内没成功返回
    @Override
    public String tryLock() {
        return this.tryLock(2000, TimeUnit.MILLISECONDS);
    }

    // 尝试加锁，可以指定尝试时间
    @Override
    public String tryLock(long tryLockTime, TimeUnit timeUnit) {
        // 是否死循环获取锁
        boolean forever = tryLockTime < 0;
        // 开始获取锁的时间
        final long startMillis = System.currentTimeMillis();
        // 转为秒
        final Long millisToWait = (timeUnit != null) ? timeUnit.toMillis(tryLockTime) : 0;

        String lockSuccess = null;
        // 如果没有加锁成功，循环尝试获取锁
        while (lockSuccess == null) {
            lockSuccess = doLock();
            // 获取成功 退出
            if (lockSuccess != null) {
                break;
            }
            // 如果超过了获取锁的最长时间，退出
            if (!forever && System.currentTimeMillis() - startMillis - retryAwait > millisToWait) {
                break;
            }
            // 睡眠重试时间，避免太频繁的重试
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryAwait));
        }
        return lockSuccess;
    }


    // 真正的加锁处理
    private String doLock() {
        String lockId = IdUtils.getUUID("");
        Jedis jedis = null;
        try {
            // 获得jedis连接
            jedis = getJedis();
            // 尝试加锁，如果成功返回OK
            // 5个参数：key value (nx)key不存在才设置 (px)设置过期时间 过期时间
            String result = getJedis().set(getKey(), lockId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME,
                    lockTimeout);
            // 加锁成功返回value值，用于解锁
            return LOCK_SUCCESS.equals(result) ? lockId : null;
        } finally {
             //关闭连接
             jedis.close();
        }
    }

    // 释放锁
    @Override
    public void unLock(String lockId) {
        // 如果get key的值和预期一样 就删除该key
        // 实际上就是保证由加锁的人来解锁
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                "then return redis.call('del', KEYS[1]) else return -1 end";
        Jedis jedis = null;
        try {
            jedis = getJedis();
            // 执行lua脚本
            jedis.eval(luaScript,
                    Collections.singletonList(getKey()), Collections.singletonList(lockId));
        } finally {
            // 关闭资源
            jedis.close();
        }
    }

    // 设置锁过期时间
    public RedisLock(int lockTimeout) {
        this.lockTimeout = lockTimeout;
    }
}