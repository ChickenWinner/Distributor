package wang.reder.distributor;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wang.reder.distributor.interfaces.IJedisOperator;
import wang.reder.distributor.lock.IDtorLock;
import wang.reder.distributor.utils.redis.JedisSimpleConfig;
import wang.reder.distributor.utils.redis.JedisSimpleOperator;

/**
 * <p>开始类 初始化系统配置<p/>
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 20:50
 */
public class Start {

    // 开始类 单例
    private final static Start startInstance = new Start();

    // 返回单例
    public static Start getInstance() {
        return startInstance;
    }

    // 构造器私有，防止被new
    private Start() {

    }

    private IJedisOperator jedisOperator;

    // 带连接池配置的初始化
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

    // 最简单的初始化
    public void initJedisConfig(String host, int port, String auth) {
        this.initJedisConfig(host, port, auth, null);
    }

    // 带Jedis连接池的初始化
    public void initJedisConfig(JedisPool jedisPool) {
        JedisSimpleOperator operator = new JedisSimpleOperator();
        operator.setJedisPool(jedisPool);
        this.jedisOperator = operator;
    }

    public IJedisOperator getJedisOperator() {
        return this.jedisOperator;
    }

    public static IDtorLock newRedisLock(String name) {
        return null;
    }

    public static IDtorLock newRedisLock(String name, int timeOut) {
        return null;
    }

}
