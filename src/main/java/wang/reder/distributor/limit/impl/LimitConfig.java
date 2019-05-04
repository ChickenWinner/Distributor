package wang.reder.distributor.limit.impl;

/**
 * <p>限流配置类<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/4 15:17
 */
public class LimitConfig {

    // 单位时间
    private int seconds;

    // 单位时间内访问次数
    private int limitAccessCount;

    public int getSeconds() {
        return seconds;
    }

    // 返回自身方便链式调用
    public LimitConfig setSeconds(int seconds) {
        this.seconds = seconds;
        return this;
    }

    public int getLimitAccessCount() {
        return limitAccessCount;
    }

    public LimitConfig setLimitAccessCount(int limitAccessCount) {
        this.limitAccessCount = limitAccessCount;
        return this;
    }



}
