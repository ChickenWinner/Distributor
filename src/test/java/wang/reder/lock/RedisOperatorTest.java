package wang.reder.lock;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wang.reder.distributor.Start;

/**
 * <p>分布式锁测试类<p/>
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 22:02
 */
public class RedisOperatorTest {

    @Test
    public void testRedisOperator() {
        Start start = Start.getInstance();
        // start.initJedisConfig("192.168.75.130", 6379, null);
        //JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),"192.168.75.130", 6379, 8000);
        start.initJedisConfig("192.168.75.130", 6379, "", new JedisPoolConfig());

        Jedis jedis = start.getJedisOperator().getJedis();
        System.out.println(jedis.get("k1"));
    }
}
