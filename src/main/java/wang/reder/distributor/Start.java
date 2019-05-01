package wang.reder.distributor;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wang.reder.distributor.interfaces.IJedisOperator;
import wang.reder.distributor.lock.IDtorLock;
import wang.reder.distributor.lock.redis.RedisLock;
import wang.reder.distributor.utils.redis.JedisSimpleConfig;
import wang.reder.distributor.utils.redis.JedisSimpleOperator;

/**
 * <p>开始类 初始化系统配置<p/>
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 20:50
 */
public class Start {

    // 开始类 单例模式
    private final static Start startInstance = new Start();

    // 返回单例
    public static Start getInstance() {
        return startInstance;
    }

    // 构造器私有，防止被new
    private Start() {

    }

    private IJedisOperator jedisOperator;

    // ----> Jedis直接连接初始化

    // 情况1：带连接池配置的初始化
    public void initJedisConfig(String host, int port, String auth,
                                JedisPoolConfig jedisPoolConfig) {
        // 设置Jedis连接属性
        // 主机 端口 密码
        JedisSimpleConfig config = new JedisSimpleConfig();
        config.setHost(host);
        config.setPort(port);
        config.setAuth(auth);
        // 初始化操作者
        JedisSimpleOperator operator = new JedisSimpleOperator();
        // 设置Jedis连接配置
        operator.setJedisConfig(config);
        // 如果有设置连接池配置
        if (jedisPoolConfig != null) {
            operator.setJedisPoolConfig(jedisPoolConfig);
        }
        operator.init();
        // 返回操作类 供下面的静态方法调用
        this.jedisOperator = operator;
    }

    // 情况2：最简单的指定主机、端口、密码（如果有） 系统提供默认的JedisPoolConfig配置
    public void initJedisConfig(String host, int port, String auth) {
        this.initJedisConfig(host, port, auth, null);
    }

    // 情况3：已经拥有JedisPool直接传入即可
    public void initJedisConfig(JedisPool jedisPool) {
        JedisSimpleOperator operator = new JedisSimpleOperator();
        operator.setJedisPool(jedisPool);
        this.jedisOperator = operator;
    }

    public IJedisOperator getJedisOperator() {
        return this.jedisOperator;
    }

    // 释放资源
    public  void destory() {
        this.jedisOperator.destroy();
    }

    // ----> RedisLock

    // 获取分布式锁，传入锁名，默认锁过期时间10s
    public static IDtorLock newRedisLock(String lockName) {
        return new RedisLock(lockName);
    }

    // 获取分布式锁，传入锁名，锁的过期时间
    public static IDtorLock newRedisLock(String lockName, int lockTimeout) {
        return new RedisLock(lockName, lockTimeout);
    }

}
