package wang.reder.distributor.limit.impl;

import redis.clients.jedis.Jedis;
import wang.reder.distributor.limit.ILimit;
import wang.reder.distributor.utils.redis.AbstractJedis;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>利用Redis实现的限流工具<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/4 15:47
 */
public class RedisLimit extends AbstractJedis implements ILimit {

    public RedisLimit() {
    }

    @Override
    public boolean accessLimit(String key, int seconds, int limitCount) {
        LimitConfig limitConfig = new LimitConfig();
        // 设置限流配置
        limitConfig.setLimitAccessCount(limitCount).
                setSeconds(seconds);
        return accessLimit(key, limitConfig);
    }

    @Override
    public boolean accessLimit(String key, LimitConfig limitConfig) {
        Jedis jedis = null;
        long count = -1;
        try {
            // 获得连接
            jedis = getJedis();
            // key属性
            List<String> keys = new ArrayList<>();
            keys.add(key);
            // argv属性
            List<String> args = new ArrayList<>();
            args.add(limitConfig.getLimitAccessCount() + "");
            args.add(limitConfig.getSeconds() + "");
            // 执行lua脚本，获得当前访问次数
            count = Long.parseLong(jedis.eval(buildLuaScript(limitConfig), keys, args) + "");
            // 超过了限流次数返回false，没超过返回true
            return count <= limitConfig.getLimitAccessCount();
        } finally {
            // 释放jedis连接资源
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    // 构造lua脚本
    private String buildLuaScript(LimitConfig limitConfig) {
        // 每次都创建一个Builder，线程隔离
        StringBuilder lua = new StringBuilder();

        lua.append("\nlocal count");
        // 根据key得到value
        lua.append("\ncount = redis.call('get',KEYS[1])");
        // 如果当前访问次数超过限流次数
        lua.append("\nif count and tonumber(count) > tonumber(ARGV[1]) then");
        // 返回访问次数
        lua.append("\nreturn count;");
        lua.append("\nend");
        // 如果没有超过访问次数，访问次数加1
        lua.append("\ncount = redis.call('incr',KEYS[1])");
        // 第一次访问，设置过期时间
        lua.append("\nif tonumber(count) == 1 then");
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        // 返回访问次数（未超过限制）
        lua.append("\nreturn count;");
        return lua.toString();
    }
}
