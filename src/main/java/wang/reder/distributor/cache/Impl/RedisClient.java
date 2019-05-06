package wang.reder.distributor.cache.Impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import wang.reder.distributor.cache.ICacheClient;
import wang.reder.distributor.utils.JsonUtil;
import wang.reder.distributor.utils.MapUtil;
import wang.reder.distributor.redis.impl.AbstractJedis;
import java.util.Collection;
import java.util.Map;

/**
 * <p>Redis缓存客户端<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/4 22:38
 */
public class RedisClient extends AbstractJedis implements ICacheClient {

    // 根据key获取value
    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            // 获得连接
            jedis = getJedis();
            // 根据key获得value
            String str = jedis.get(key);
            // String -> 对应的类型
            T t = JsonUtil.stringToBean(str, clazz);
            return t;
        } finally {
            // 释放连接
            if(jedis != null)
            jedis.close();
        }
    }

    // 批量获取value
    @Override
    public Map<String, String> getBatch(Collection<String> keys) {
        Pipeline pip = getJedis().pipelined();
        // Response的Map
        Map<String, Response<String>> respMap = MapUtil.newHashMap(keys.size());
        // 返回的结果Map
        Map<String, String> result = MapUtil.newHashMap(keys.size());

        // 根据key批量获取value
        // 注意：如果这样写会报错 keys.forEach(key -> resultMap.put(key, pip.get(key).get()));
        keys.forEach(key -> respMap.put(key, pip.get(key)));
        // 等待全部get完成
        pip.sync();
        // 将respMap -> resultMap
        respMap.forEach((key, response) -> result.put(key, response.get()));
        return result;
    }

    // 设置key-value
    @Override
    public <T> boolean set(String key, int seconds, T value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            // 对象 -> String
            String str = JsonUtil.beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            // 如果没有设置过期时间
            if (seconds <= 0) {
                jedis.set(key, str);
            } else {
                // 如果设置了过期时间
                jedis.setex(key, seconds, str);
            }
            return true;
        } finally {
            if(jedis != null)
                jedis.close();
        }
    }

    // 批量设置key-value
    @Override
    public <V> boolean setBatch(Map<String, V> map, int seconds) {
        Map<String, String> data = MapUtil.newHashMap(map.size());
        // 构造新map, 将v -> String
        map.forEach((key, value) -> data.put(key, JsonUtil.beanToString(value)));

        Pipeline pip = getJedis().pipelined();
        // 如果没有设置过期时间
        if(seconds <= 0) {
            data.forEach((key, value) -> pip.set(key, value));
        } else {
            // 如果设置了过期时间
            data.forEach((key,value) -> pip.setex(key, seconds, value));
        }
        // 等待全部set完成
        pip.sync();
        return true;
    }

    // 删除指定的key
    @Override
    public void del(String key) {
        getJedis().del(key);
    }

    // 批量删除key
    @Override
    public void delBatch(Collection<String> keys) {
        Pipeline pip = getJedis().pipelined();
        // 批量删除key
        keys.forEach(key -> pip.del(key));
        // 等待全部删除完成
        pip.sync();
    }

    // 判断key是否存在
    @Override
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

    // key 指定key加integer
    @Override
    public Long incrBy(String key, long integer) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.incrBy(key, integer);
        } finally {
            if(jedis != null)
                jedis.close();
        }
    }

    // key 指定key减integer
    @Override
    public Long decrBy(String key, long integer) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.decrBy(key, integer);
        } finally {
            if(jedis != null)
                jedis.close();
        }
    }

}
