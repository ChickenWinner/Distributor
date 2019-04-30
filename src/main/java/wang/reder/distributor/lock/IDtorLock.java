package wang.reder.distributor.lock;

import java.util.concurrent.locks.Lock;

/**
 * <p>分布式锁接口<p/>
 * <p>该接口继承了Java api中Lock接口，目的在于无缝衔接Java中Lock的使用<p/>
 * <p>不直接让实现类实现Java中Lock接口的原因是让自己的实现类只实现自己的接口<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 19:57
 */
public interface IDtorLock extends Lock {

    /**
     * 释放资源
     */
    void close();

}
