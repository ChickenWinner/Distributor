package wang.reder.distributor.cache.Impl;

import redis.clients.jedis.Jedis;
import wang.reder.distributor.cache.IRedisCache;
import wang.reder.distributor.utils.JsonUtil;
import wang.reder.distributor.utils.redis.AbstractJedis;

/**
 * <p>Redis缓存客户端<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/4 22:38
 */
public class RedisCache extends AbstractJedis implements IRedisCache {


    /**
     * 获取一个Jedis连接
     *
     * @return 一个redis连接
     */
    public Jedis getJedisClient() {return getJedis();}


    /**
     * 根据key获取值
     *
     * @param key   key值
     * @param clazz 获取到的结果类型
     * @param <T>   类型泛型
     * @return 返回结果
     */
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String str = jedis.get(key);
            T t = JsonUtil.stringToBean(str, clazz);
            return t;
        } finally {
            if(jedis != null)
            jedis.close();
        }
    }

    /**
     * 设置key value
     *
     * @param key           key值
     * @param expireSeconds 过期时间 <=0为不过期
     * @param value         value值，如果是对象，会将对象转为String进行存储
     * @param <T>           泛型
     * @return 是否设置成功
     */
    public <T> boolean set(String key, int expireSeconds, T value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String str = JsonUtil.beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            if (expireSeconds <= 0) {
                jedis.set(key, str);
            } else {
                jedis.setex(key, expireSeconds, str);
            }
            return true;
        } finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * @param key key值
     * @return 指定的key是否存在
     */
    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.exists(key);
        } finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * @param key 指定key加1
     * @return 加1后的结果
     */
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.incr(key);
        } finally {
            if(jedis != null)
                jedis.close();
        }
    }


    /**
     * @param key 指定key减1
     * @return 减1后的结果
     */
    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.decr(key);
        } finally {
            if(jedis != null)
                jedis.close();
        }
    }


}
