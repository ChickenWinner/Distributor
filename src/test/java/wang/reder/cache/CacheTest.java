package wang.reder.cache;

import org.junit.Test;
import wang.reder.distributor.Distributor;
import wang.reder.distributor.cache.ICacheClient;
import wang.reder.distributor.utils.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <P>缓存操作测试<P/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/5 15:05
 */
public class CacheTest {

    @Test
    public void redisClientTest() {

        Person p = new Person();

        Map<String, Object> data = MapUtil.newHashMap(2);
        data.put("p", p);
        data.put("n", 1);

//        List<String> list = new ArrayList<>();
//        list.add("p");
//        list.add("key");

        Distributor distributor = Distributor.getInstance();
        distributor.initJedisConfig("xxx", 6379, "");

        ICacheClient cacheClient = Distributor.newCacheClient();
        cacheClient.set("p", -1, p);


        // cacheClient.setBatch(data, -1);
        // System.out.println(cacheClient.incrBy("n", 10));
        System.out.println(cacheClient.decrBy("n", 10));
    }
}
