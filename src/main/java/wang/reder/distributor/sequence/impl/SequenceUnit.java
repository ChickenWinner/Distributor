package wang.reder.distributor.sequence.impl;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>序列单元<p/>
 * <p>按照序列单元来分配序列，既可以减少范围Redis的次数，同时也是Redis序列生成器的关键点<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 15:06
 */
public class SequenceUnit {

    // 单元的开始位置
    private final long begin;

    // 单元的结束位置
    private final long end;

    // 当前序列号
    private final AtomicLong currentSeq;

    // 单元是否结束
    private volatile boolean isEnd = false;

    SequenceUnit(long begin, long end) {
        this.begin = begin;
        this.end = end;
        this.currentSeq = new AtomicLong(begin);
    }


    /**
     * 返回当前序列，并加1(成为下一个序列)
     *
     * @return 序列号，如果返回-1表示单元序列分配完毕，需要重新申请一块单元
     */
    long getAndIncrement() {
        long seq = currentSeq.getAndIncrement();
        if (seq > end) {
            isEnd = true;
            return -1;
        }
        return seq;
    }

    // 判断单元是否到尾了
    boolean isEnd() {
        return isEnd;
    }


}
