package wang.reder.distributor.sequence;


import wang.reder.distributor.exception.GetSeqException;

/**
 * <p>生成id接口<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 10:46
 */
public interface ISequence {

    /**
     * 生成下一个序列号
     *
     * @return 序列号
     * @throws GetSeqException 获得序列异常
     */
    long nextId() throws GetSeqException;
}
