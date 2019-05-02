package wang.reder.distributor.exception;

/**
 * <p>获得序列异常<p/>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/5/2 10:45
 */
public class GetSeqException extends RuntimeException{

    public GetSeqException(String message) {
        super(message);
    }

    public GetSeqException(Throwable cause) {
        super(cause);
    }
}
