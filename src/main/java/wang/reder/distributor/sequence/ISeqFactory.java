package wang.reder.distributor.sequence;

/**
 * <p>序列生成器工厂接口<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 10:47
 */
public interface ISeqFactory {

    /**
     * 生产一个序列号生成器
     *
     * @return 序列号生成器
     */
    ISequence produce();
}
