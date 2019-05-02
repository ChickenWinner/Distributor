package wang.reder.distributor.sequence.impl;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>序列单元<p/>
 * <p>使用序列单元的意义在于不用每次都去Redis中查找<p/>
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
     * @return 序列号，如果返回-1该单元内的序列号分配完
     */
    long getAndIncrement() {
        long seq = currentSeq.getAndIncrement();
        if (seq > end) {
            isEnd = true;
            return -1;
        }
        return seq;
    }

    // 判断单元是否结束
    boolean isEnd() {
        return isEnd;
    }


}
