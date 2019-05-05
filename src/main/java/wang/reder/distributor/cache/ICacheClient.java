package wang.reder.distributor.cache;

import java.util.Collection;
import java.util.Map;

/**
 * <p>缓存客户端操作接口<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/4 22:36
 */
public interface ICacheClient {


    /**
     * 根据key获得value
     *
     * @param key   key值
     * @param clazz 将获得的value转为对应的类型
     * @param <T>   泛型
     * @return value，可指定类型
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 批量获取value
     *
     * @param keys key集合
     * @return 返回结果Map，key为Redis中的key value为对应的value
     */
    Map<String, String> getBatch(Collection<String> keys);

    /**
     * 设置key-value
     *
     * @param key     key值
     * @param seconds 过期时间 <=0为不过期
     * @param value   value值，如果是对象，会将对象转为String进行存储
     * @param <T>     泛型
     * @return 是否设置成功
     */
    <T> boolean set(String key, int seconds, T value);

    /**
     * 批量设置key-value
     *
     * @param map     要设置的k-v
     * @param seconds 过期时间 小于等于0不设置
     * @param <V>     value泛型，支持任意类型
     * @return 是否设置成功
     */
    <V> boolean setBatch(Map<String, V> map, int seconds);

    /**
     * 删除指定的Key
     *
     * @param key key值
     */
    void del(String key);


    /**
     * 批量删除key
     *
     * @param keys key集合
     */
    void delBatch(Collection<String> keys);

    /**
     * 判断指定的key是否存在
     *
     * @param key key值
     * @return true：存在 false：不存在
     */
    boolean exists(String key);

    /**
     * key加上指定integer
     *
     * @param key     key值
     * @param integer 数字
     * @return 加上后的结果
     */
    Long incrBy(String key, long integer);

    /**
     * key减去指定integer
     *
     * @param key     key值
     * @param integer 数字
     * @return 减去后的结果
     */
    Long decrBy(String key, long integer);
}
