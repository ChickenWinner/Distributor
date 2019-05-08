package wang.reder.distributor.redis;
import redis.clients.jedis.Jedis;


/**
 * <p>Jedis操作接口<p/>
 * <p>主要用于直接连接的Jedis操作<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 20:19
 */
public interface IJedisOperator {

    /**
     * 初始化
     */
    void init();

    /**
     * 获得一个Jedis连接
     * @return Jedis连接
     */
    Jedis getJedis();

    /**
     * 释放资源
     */
    void destroy();
}
