package wang.reder.distributor.lock;

import wang.reder.distributor.interfaces.ICloseable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <p>分布式锁接口<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 19:57
 */
public interface IDtorLock {

    String lock();

    String tryLock();

    String tryLock(long tryLockTime, TimeUnit timeUnit);

    void unLock(String lockId);
}
