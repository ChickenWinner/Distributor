package wang.reder.sequence;

import org.junit.Test;
import wang.reder.distributor.sequence.ISequence;
import wang.reder.distributor.sequence.factory.SnowflakeFactory;

/**
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 11:56
 */
public class SeqTest {

    @Test
    public void SnowflakeSeqTest() {
        System.out.println(System.currentTimeMillis());
        long startTime = System.nanoTime();
        ISequence sequence = SnowflakeFactory.instance().param(1,2).produce();
        for (int i = 0; i < 50000; i++) {
            long id = sequence.nextId();
            System.out.println(id);
        }
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");
    }
}
