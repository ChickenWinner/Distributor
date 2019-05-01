package wang.reder.lock;

import org.junit.Test;
import wang.reder.distributor.Start;
import wang.reder.distributor.lock.IDtorLock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/1 12:55
 */
public class LockTest {

    @Test
    public void redisLockTest() throws Exception {
        // 获得实例
        Start start = Start.getInstance();
        // 连接配置
        start.initJedisConfig("xxx", 6379, "");

        CountDownLatch downLatch = new CountDownLatch(20);
        // 获得锁
        IDtorLock lock = Start.newRedisLock("myLock");

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    // 尝试加锁
                    String kId = lock.tryLock(3000, TimeUnit.MILLISECONDS);
                    if (null != kId) {
                        System.out.println("lockId:" + kId);
                        lock.unLock(kId);
                    }
                }
                downLatch.countDown();

            }).start();
        }
        downLatch.await();


        start.destory();
    }


    @Test
    public void redisReentrantLockTest() throws Exception {
        // 获得实例
        Start start = Start.getInstance();
        // 连接配置
        start.initJedisConfig("xxx", 6379, "");

        CountDownLatch downLatch = new CountDownLatch(20);
        // 获得锁
        IDtorLock lock = Start.newRedisReentrantLock("myLock");

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    // 尝试加锁
                    String kId = lock.lock();
                    if (null != kId) {
                        System.out.println("lockId:" + kId);
                        lock.unLock(kId);
                    }
                }
                downLatch.countDown();

            }).start();
        }
        downLatch.await();

        start.destory();
    }
}
