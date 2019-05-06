package wang.reder.sequence;

import org.junit.Test;
import wang.reder.distributor.Distributor;
import wang.reder.distributor.sequence.ISequence;

import java.util.concurrent.CountDownLatch;

/**
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 11:56
 */
public class SeqTest {

    @Test
    public void snowflakeSeqTest() {
        // 获得雪花序列生成器
        ISequence sequence = Distributor.newSnowflakeSeq(21, 21);
        long startTime = System.nanoTime();
        // 生成五十万个ID
        for (int i = 0; i < 500000; i++) {
            long id = sequence.nextId();
            // System.out.println(id);
        }
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");
    }

    @Test
    public void redisSeqTest() throws InterruptedException {
        Distributor distributor = Distributor.getInstance();
        // 连接配置
        distributor.initJedisConfig("192.168.75.133", 6379, "");

        ISequence sequence = Distributor.newRedisSeq("seq", 5000, 1);

        long startTime = System.nanoTime();
        for (int i = 0; i < 500000; i++) {
            long id = sequence.nextId();
            // System.out.println(id);
        }

        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");
    }
}
