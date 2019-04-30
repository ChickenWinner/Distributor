package wang.reder.distributor.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wang.reder.distributor.interfaces.IJedisOperator;

/**
 * <p>通用Jedis操作<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 20:29
 */
public class JedisSimpleOperator implements IJedisOperator {

    // 这3个私有属性对应3种不同的构造方式
    // Jedis简单配置 host port auth
    private JedisSimpleConfig jedisConfig;

    // Jedis连接池
    private JedisPool jedisPool;

    // Jedis连接池配置
    private JedisPoolConfig jedisPoolConfig;

    // 最简单的初始化
    @Override
    public void init() {
        // 如果传入了连接池
        if (jedisPool != null) {
            return ;
        } else if (jedisPoolConfig != null) {
            // do nothing
        } else {
            jedisPoolConfig = new JedisPoolConfig();
        }
        // 初始化线程池
        jedisPool = new JedisPool(jedisPoolConfig, jedisConfig.getHost(), jedisConfig.getPort(), 8000);
    }


    @Override
    public Jedis getJedis() {
        // 从连接池获得一个连接
        Jedis jedis = jedisPool.getResource();
        // 当配置不为空时，获得配置中的密码
        String auth = null;
        if(jedisConfig != null) {
            auth = jedisConfig.getAuth();
        }
        // 当密码不为空时设置密码
        if (auth != null && !"".equals(auth)) {
            jedis.auth(jedisConfig.getAuth());
        }
        return jedis;
    }

    @Override
    public void destroy() {
        // 释放线程池资源
        if (this.jedisPool != null) {
            this.jedisPool.destroy();
        }
    }

    public void setJedisConfig(JedisSimpleConfig jedisConfig) {
        this.jedisConfig = jedisConfig;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }
}
