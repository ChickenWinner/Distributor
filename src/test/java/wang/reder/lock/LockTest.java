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
        Start start = Start.getInstance();
        start.initJedisConfig("192.168.75.130", 6379, "");
        CountDownLatch downLatch = new CountDownLatch(20);

        IDtorLock lock = Start.newRedisLock("myLock");

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    String kId = lock.tryLock(3000, TimeUnit.MILLISECONDS);
                    if (null != kId) {
                        System.out.println("kId:" + kId);
                        lock.unLock(kId);
                    }
                }
                downLatch.countDown();

            }).start();
        }
        downLatch.await();
    }
    }
