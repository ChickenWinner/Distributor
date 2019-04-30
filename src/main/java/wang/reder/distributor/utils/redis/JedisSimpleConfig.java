package wang.reder.distributor.utils.redis;

/**
 * <p>Jedis简单配置类，包含常用的Redis属性<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 20:30
 */
public class JedisSimpleConfig {

    // 主机号 如127.0.0.1
    private String host;

    // 端口号 如6379
    private int port;

    // 密码，非必须项
    private String auth;

    // ----->下面为get、set方法
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
