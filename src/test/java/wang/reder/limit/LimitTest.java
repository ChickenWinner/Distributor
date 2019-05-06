package wang.reder.limit;

import org.junit.Test;
import wang.reder.distributor.Distributor;
import wang.reder.distributor.limit.ILimit;

/**
 * <p>限流工具测试<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/4 15:59
 */
public class LimitTest {

    @Test
    public void RedisLimitTest() throws InterruptedException {
        Distributor distributor = Distributor.getInstance();
        // 连接配置
        distributor.initJedisConfig("xxx", 6379, "");

        ILimit limit = Distributor.newAccessLimit();

        for(int i = 0; i < 30; i++) {
            // 十秒内只能访问一次
            System.out.println(limit.accessLimit("limit", 10, 1));
            Thread.sleep(100);
        }

    }
}
