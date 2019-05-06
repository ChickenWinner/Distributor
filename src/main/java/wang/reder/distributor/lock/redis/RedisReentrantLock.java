package wang.reder.distributor.lock.redis;


import wang.reder.distributor.lock.ILock;
import wang.reder.distributor.redis.impl.AbstractJedis;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>基于Redis实现的分布式可重复性锁<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/1 19:00
 */
public class RedisReentrantLock extends AbstractJedis implements ILock {

    // 存储每个线程获取锁的信息
    private final ConcurrentHashMap<Thread, LockData> threadData = new ConcurrentHashMap<>();

    // 普通Redis分布式锁
    private RedisLock redisLock;

    // new一个可重入锁，指定名字
    public RedisReentrantLock(String lockName) {
        redisLock = new RedisLock(lockName);
    }

    // new一个可重入锁，指定名字和锁过期时间
    public RedisReentrantLock(String lockName, int lockTimeout) {
        redisLock = new RedisLock(lockName, lockTimeout);
    }

    // 锁信息
    private static class LockData {
        // 当前线程
        final Thread currentThread;
        // value
        final String lockId;
        // 加锁次数
        final AtomicInteger lockCount = new AtomicInteger(1);

        private LockData(Thread currentThread, String lockVal) {
            this.currentThread = currentThread;
            this.lockId = lockVal;
        }

        private String getLockId() {
            return this.lockId;
        }
    }


    @Override
    public String lock() {
        return this.tryLock(-1, null);
    }

    @Override
    public String tryLock() {
        return this.tryLock(2000, TimeUnit.MILLISECONDS);
    }

    @Override
    public String tryLock(long tryLockTime, TimeUnit unit){
        // 获得当前线程
        Thread currentThread = Thread.currentThread();
        // 获取当前线程的锁信息
        LockData lockData = threadData.get(currentThread);
        String lockId = null;
        // 该线程已经拥有锁，加锁次数加1
        if (lockData != null) {
            lockData.lockCount.incrementAndGet();
            return threadData.get(currentThread).getLockId();
        } else {
            // 第一次加锁
            lockId = redisLock.tryLock(tryLockTime, unit);
            // 加锁成功，记录下当前线程
            if (lockId != null) {
                LockData newLockData = new LockData(currentThread, lockId);
                threadData.put(currentThread, newLockData);
                return lockId;
            }
        }
        // 如果在这里return了，说明加锁失败
        return lockId;
    }

    @Override
    public void unLock(String lockId) {
        // 获得当前线程
        Thread currentThread = Thread.currentThread();
        // 获得当前线程加锁信息
        LockData lockData = threadData.get(currentThread);
        // 没有找到锁信息，抛出异常
        if (lockData == null) {
            throw new IllegalMonitorStateException("该线程未持有锁！无法释放：" + lockId);
        }
        // 加锁次数-1
        int newLockCount = lockData.lockCount.decrementAndGet();
        // 如果还持有锁，返回
        if (newLockCount > 0) {
            return;
        }
        // 释放锁异常
        if (newLockCount < 0) {
            throw new IllegalMonitorStateException("释放锁异常: " + lockId);
        }
        try {
            // 释放掉锁
            redisLock.unLock(lockId);
        } finally {
            // 删除该锁信息
            threadData.remove(currentThread);
        }
    }

}
