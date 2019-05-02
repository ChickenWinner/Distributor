package wang.reder.distributor.sequence.impl;

import redis.clients.jedis.Jedis;
import wang.reder.distributor.exception.GetSeqException;
import wang.reder.distributor.sequence.ISequence;
import wang.reder.distributor.utils.redis.AbstractJedis;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 15:06
 */
public class RedisSequence extends AbstractJedis implements ISequence {

    // 默认区间跨度
    private final static int DEFAULT_UNIT_STEP = 2000;

    // 默认序列号起始位置
    private final static int DEFAULT_UNIT_START = 0;

    // 锁
    private final Lock lock = new ReentrantLock();

    // 单元长度
    private int step;

    // 开始位置
    private long stepStart;

    // key是否存在
    private volatile boolean isKeyExist;

    // 序列单元
    private volatile SequenceUnit sequenceUnit;

    // 构造函数
    public RedisSequence(String key, int step, long stepStart) {
        // 如果输入负数，则使用默认设置
        if(step <= 0) {
            step = DEFAULT_UNIT_STEP;
        }
        if(stepStart < 0) {
            stepStart = DEFAULT_UNIT_START;
        }
        // 设置Key
        setKey(key);
        // 设置单元长度
        this.step = step;
        // 设置开始位置
        this.stepStart = stepStart;
    }

    // 获得ID
    @Override
    public long nextId() throws GetSeqException {
        // 得到key
        String key = getKey();

        // 双重检测，如果单元不存在，获取一个
        // 只会发生在第一次请求序列，或者重启服务
        if (sequenceUnit == null) {
            lock.lock();
            try {
                if (sequenceUnit == null) {
                    sequenceUnit = nextUnit(key);
                }
            } finally {
                lock.unlock();
            }
        }

        // 当value值为-1时，表明区间的序列号已经分配完，需要重新获取单元
        long seq = sequenceUnit.getAndIncrement();
        // 单元分配完毕，重新申请
        if (seq == -1) {
            lock.lock();
            try {
                // 循环获取，直到成功
                while (true){
                    // 重新分配单元
                    if (sequenceUnit.isEnd()) {
                        sequenceUnit = nextUnit(getKey());
                    }
                    seq = sequenceUnit.getAndIncrement();
                    if (seq == -1) {
                        continue;
                    }
                    break;
                }
            } finally {
                lock.unlock();
            }
        }
        if (seq < 0) {
            throw new GetSeqException("获取序列异常：" + seq);
        }
        return seq;
    }

    // 分配单元区间
    private SequenceUnit nextUnit(String key) {
        Jedis jedis = null;
        try {
            // 获得连接
            jedis = getJedis();
            // 第一次要设置Key
            if (!isKeyExist) {
                Boolean isExists = jedis.exists(key);
                if (!isExists) {
                    // 第一次不存在，进行初始化,setnx不存在就set，存在就忽略
                    jedis.setnx(key, String.valueOf(stepStart));
                }
                isKeyExist = true;
            }
            // 一次性取走一个单元区间，保证一个区间内的序列分配给一个服务器使用
            Long end = getJedis().incrBy(key, step);
            // 计算出区间开始位置
            Long begin = end - step + 1;
            return new SequenceUnit(begin, end);
        } finally {
            if(jedis != null)
            jedis.close();
        }
    }


}
