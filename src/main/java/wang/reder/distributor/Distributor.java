package wang.reder.distributor;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wang.reder.distributor.cache.ICacheClient;
import wang.reder.distributor.cache.Impl.RedisClient;
import wang.reder.distributor.redis.IJedisOperator;
import wang.reder.distributor.limit.ILimit;
import wang.reder.distributor.limit.impl.RedisLimit;
import wang.reder.distributor.lock.ILock;
import wang.reder.distributor.lock.redis.RedisLock;
import wang.reder.distributor.lock.redis.RedisReentrantLock;
import wang.reder.distributor.sequence.ISequence;
import wang.reder.distributor.sequence.impl.RedisSequence;
import wang.reder.distributor.sequence.impl.SnowflakeSequence;
import wang.reder.distributor.redis.impl.JedisOperatorInit;

/**
 * <p>门面模式，调度系统功能<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 20:50
 */
public class Distributor {

    // 开始类 单例模式
    private final static Distributor startInstance = new Distributor();

    // jedis操作类，用于Jedis操作
    private IJedisOperator jedisOperator;

    // JedisOperator初始化类
    private JedisOperatorInit jedisInit = new JedisOperatorInit();

    // 返回单例
    public static Distributor getInstance() {
        return startInstance;
    }

    // 构造器私有，防止被new
    private Distributor() {

    }

    // ----> Jedis直接连接初始化

    // 情况1：带连接池配置的初始化
    public void initJedisConfig(String host, int port, String auth,
                                JedisPoolConfig jedisPoolConfig) {
        this.jedisOperator = jedisInit.initJedisConfig(host, port, auth, jedisPoolConfig);
    }

    // 情况2：最简单的指定主机、端口、密码（如果有） 系统提供默认的JedisPoolConfig配置
    public void initJedisConfig(String host, int port, String auth) {
        this.initJedisConfig(host, port, auth, null);
    }

    // 情况3：已经拥有JedisPool直接传入即可
    public void initJedisConfig(JedisPool jedisPool) {
        this.jedisOperator = jedisInit.initJedisConfig(jedisPool);
    }

    public IJedisOperator getJedisOperator() {
        return this.jedisOperator;
    }

    // 销毁JedisPool
    public void destory() {
        this.jedisOperator.destroy();
    }

    // ----> RedisLock

    // 获取分布式锁，传入锁名，默认锁过期时间10s
    public static ILock newRedisLock(String lockName) {
        return new RedisLock(lockName);
    }

    // 获取分布式锁，传入锁名，锁的过期时间
    public static ILock newRedisLock(String lockName, int lockTimeout) {
        return new RedisLock(lockName, lockTimeout);
    }

    // 获取分布式锁，传入锁名，默认锁过期时间10s
    public static ILock newRedisReentrantLock(String lockName) {
        return new RedisReentrantLock(lockName);
    }

    // 获取分布式锁，传入锁名，锁的过期时间
    public static ILock newRedisReentrantLock(String lockName, int lockTimeout) {
        return new RedisReentrantLock(lockName, lockTimeout);
    }

    // ----> Sequence

    // 得到雪花序列生成器 可以指定工作ID和数据中心ID
    public static ISequence newSnowflakeSeq(long workerId, long datacenterId) {
        return new SnowflakeSequence(workerId, datacenterId);
    }

    // 使用默认的工作ID和数据中心ID
    public static ISequence newSnowflakeSeq() {
        return new SnowflakeSequence(-1, -1);
    }

    // 指定key，单元长度和开始位置默认
    public static ISequence newRedisSeq(String key) {
        return new RedisSequence(key, -1, -1);
    }

    // 指定key 单元长度 开始位置
    public static ISequence newRedisSeq(String key, int step, long stepStart) {
        return new RedisSequence(key, step, stepStart);
    }

    // ----> Limit
    public static ILimit newAccessLimit() {
        return new RedisLimit();
    }

    // ----> Cache
    public static ICacheClient newCacheClient() {
        return new RedisClient();
    }
}
