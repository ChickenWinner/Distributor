package wang.reder.distributor.utils.redis;

import redis.clients.jedis.Jedis;
import wang.reder.distributor.Distributor;
import wang.reder.distributor.interfaces.IJedisOperator;

/**
 * <p>Jedis调用抽象类, 对Jedis操作再进行一层封装<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 21:40
 */
public abstract class AbstractJedis {

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

    // 获得一个Jedis连接
    protected Jedis getJedis() {
        // 如果jedis操作类还没有初始化
        if (jedisOperator == null) {
            // 从开始类中获取Jedis操作类
            jedisOperator = Distributor.getInstance().getJedisOperator();
        }
        return jedisOperator.getJedis();
    }


}
