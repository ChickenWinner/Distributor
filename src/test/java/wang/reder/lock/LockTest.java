package wang.reder.lock;

import org.junit.Test;
import wang.reder.distributor.Distributor;
import wang.reder.distributor.lock.ILock;
import java.util.concurrent.CountDownLatch;

/**
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/1 12:55
 */
public class LockTest {

    @Test
    public void redisLockTest() throws Exception {
        // 获得实例
        Distributor distributor = Distributor.getInstance();
        // 连接配置
        distributor.initJedisConfig("xxx", 6379, "");

        CountDownLatch downLatch = new CountDownLatch(20);
        // 获得锁
        ILock lock = Distributor.newRedisLock("myLock");

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    // 尝试加锁
                    String lockId = lock.lock();
                    if (lockId != null) {
                        // System.out.println("lockId:" + lockId);
                        lock.unLock(lockId);
                    }
                }
                downLatch.countDown();

            }).start();
        }
        downLatch.await();

        distributor.destory();
    }


    @Test
    public void redisReentrantLockTest() throws Exception {
        // 获得实例
        Distributor distributor = Distributor.getInstance();
        // 连接配置
        distributor.initJedisConfig("xxx", 6379, "");

        CountDownLatch downLatch = new CountDownLatch(20);
        // 获得锁
        ILock lock = Distributor.newRedisReentrantLock("myLock");

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    // 尝试加锁
                    String lockId = lock.lock();
                    if (lockId != null) {
                        System.out.println("lockId:" + lockId);
                        lock.unLock(lockId);
                    }
                }
                downLatch.countDown();

            }).start();
        }
        downLatch.await();

        distributor.destory();
    }

}
