package wang.reder.distributor.lock;


import java.util.concurrent.TimeUnit;

/**
 * <p>分布式锁接口<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 19:57
 */
public interface IDtorLock {

    /**
     * 获取锁直到成功
     *
     * @return 成功lockId，用来解锁
     */
    String lock();

    /**
     * 尝试获取锁，如果在2s内没获取成功，返回
     *
     * @return 成功：<code>return lockId<coe/>
     * 失败：<code>return null<code/>
     */
    String tryLock();

    /**
     * 尝试获取锁 可以指定尝试时间
     *
     * @param tryLockTime 尝试获取时间
     * @param timeUnit    时间单位
     * @return 成功：<code>return lockId<coe/>
     * 失败：<code>return null<code/>
     */
    String tryLock(long tryLockTime, TimeUnit timeUnit);

    /**
     * 释放锁
     *
     * @param lockId 加锁成功返回的lockId
     */
    void unLock(String lockId);
}
