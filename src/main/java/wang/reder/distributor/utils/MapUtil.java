package wang.reder.distributor.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Map工具类<p/>A
 *
 * Created by xuan on 2018/8/13.
 */
public abstract class MapUtil {

    /**
     * 构建一个MAP，指定预计存储数量
     *
     * @param expectedSize 预计存储数量
     * @param <K>          key
     * @param <V>          value
     * @return 空MAP
     */
    public static <K, V> Map<K, V> newHashMap(int expectedSize) {
        return new HashMap<>(capacity(expectedSize));
    }

    /**
     * 计算MAP实际数量，默认扩展因子是0.75
     *
     * @param expectedSize 预计存储数量
     * @return MAP实际数量
     */
    private static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            return expectedSize + 1;
        }
        return (int) ((float) expectedSize / 0.75F + 1.0F);
    }

}
