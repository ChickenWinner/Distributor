package wang.reder.distributor.utils.redis;

import redis.clients.jedis.Jedis;
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
public abstract class JedisBase implements ICloseable {

    // Jedis操作类
    private IJedisOperator jedisOperator;

    // Jedis连接
    private Jedis jedis;

    // key值
    private String key;

    protected String getKey() {
        return key;
    }

    protected void setKey(String key) {
        this.key = key;
    }

    protected Jedis getJedis() {
        // 如果jedis为空，先初始化
        if(this.jedis == null) {
            this.initJedis();
        }
        return jedis;
    }

    private void initJedis() {
        // 如果操作类为空，先初始化操作类
        if (jedisOperator == null) {
            this.jedisOperator = Start.getInstance().getJedisOperator();
            this.jedis = jedisOperator.getJedis();
        } else {
            this.jedis = jedisOperator.getJedis();
        }
    }

    @Override
    public void close() {
        // 释放Jedis连接
        if (this.getJedis() != null) {
            this.getJedis().close();
        }
    }

}
