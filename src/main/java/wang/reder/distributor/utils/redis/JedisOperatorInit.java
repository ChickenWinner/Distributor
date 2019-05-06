package wang.reder.distributor.utils.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wang.reder.distributor.interfaces.IJedisOperator;
import wang.reder.distributor.utils.redis.JedisSimpleConfig;
import wang.reder.distributor.utils.redis.JedisSimpleOperator;

/**
 * <p>Jedis系统配置<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/6 11:51
 */
public class JedisOperatorInit {


    // 情况1：带连接池配置的初始化
    public IJedisOperator initJedisConfig(String host, int port, String auth,
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
        return operator;
    }

    // 情况2：已经拥有JedisPool直接传入即可
    public IJedisOperator initJedisConfig(JedisPool jedisPool) {
        JedisSimpleOperator operator = new JedisSimpleOperator();
        operator.setJedisPool(jedisPool);
        return operator;
    }
}
