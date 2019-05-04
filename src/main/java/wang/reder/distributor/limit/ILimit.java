package wang.reder.distributor.limit;

import wang.reder.distributor.limit.impl.LimitConfig;

/**
 * <p>限流接口<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/4 15:16
 */
public interface ILimit {

    /**
     * <p>访问资源，单位时间内只能范围limitCount次<p/>
     * @param key key
     * @param seconds 单位时间
     * @param limitCount 单位时间内限制次数
     * @return 是否访问成功
     */
    boolean accessLimit(String key,int seconds,int limitCount);

    /**
     * <p>访问资源，单位时间内只能范围limitCount次</p>
     * <p>可以自定义限流配置<p/>
     * @param key 资源
     * @param limitConfig 限流配置
     * @return 是否访问成功
     */
    boolean accessLimit(String key, LimitConfig limitConfig);
}
