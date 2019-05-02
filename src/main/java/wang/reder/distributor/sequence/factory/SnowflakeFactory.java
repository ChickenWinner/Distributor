package wang.reder.distributor.sequence.factory;

import wang.reder.distributor.sequence.ISeqFactory;
import wang.reder.distributor.sequence.ISequence;
import wang.reder.distributor.sequence.impl.SnowflakeSequence;
import wang.reder.distributor.utils.SnowflakeUtil;

/**
 * <p>基于雪花序列生成器工厂<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 11:28
 */
public class SnowflakeFactory implements ISeqFactory {

    /**
     * 数据中心ID，值的范围在[0,31]之间，一般可以设置机房的IDC[必选]
     */
    private long datacenterId;
    /**
     * 工作机器ID，值的范围在[0,31]之间，一般可以设置机器编号[必选]
     */
    private long workerId;

    // 生产一个序列生成器
    @Override
    public ISequence produce() {
        // 如果没有手动设置工作ID和数据中心，采用默认值
        if(workerId == 0) {
            this.workerId = SnowflakeUtil.getWorkId();
        }
        if(datacenterId == 0) {
            this.datacenterId = SnowflakeUtil.getWorkId();
        }
        return new SnowflakeSequence(workerId, datacenterId);
    }

    // 实例化工厂
    public static SnowflakeFactory instance() {
        return new SnowflakeFactory();
    }

    // 设置属性
    public SnowflakeFactory param(long workerId, long datacenterId) {
        // 设置工作ID
        this.workerId = workerId;
        // 设置数据中心ID
        this.datacenterId = datacenterId;
        // 链式调用
        return this;
    }

}
