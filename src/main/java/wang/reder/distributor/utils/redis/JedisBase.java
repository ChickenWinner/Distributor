package wang.reder.distributor.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import wang.reder.distributor.Start;
import wang.reder.distributor.interfaces.ICloseable;
import wang.reder.distributor.interfaces.IJedisOperator;

/**
 * <p>Jedis调用抽象类<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 21:40
 */
public abstract class JedisBase {

    // Jedis操作类
    private IJedisOperator jedisOperator;

    // key值
    private String key;

    protected String getKey() {
        return key;
    }

    protected void setKey(String key) {
        this.key = key;
    }

    protected Jedis getJedis() {
        if (jedisOperator == null) {
            jedisOperator = Start.getInstance().getJedisOperator();
        }
        return jedisOperator.getJedis();
    }


}
