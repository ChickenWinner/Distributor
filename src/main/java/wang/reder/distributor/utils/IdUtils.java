package wang.reder.distributor.utils;

import java.util.UUID;

/**
 * <p>生成随机ID工具方法</p>
 *
 * @author Red
 * email: 1318944013@qq.com
 * date: 2019/4/30 20:12
 */
public class IdUtils {

    /**
     * 生成一个UUID
     * 可根据需求替换掉UUID默认的'-'
     * @param replaceStr 替换字符 有以下情况
     *                  1.null不替换  2.''，将'-'删除  3.其他字符，将'-'替换为该字符
     * @return 一个UUID字符串
     */
    public static String getUUID(String replaceStr){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        if(replaceStr == null) {
            return uuid;
        } else if("".equals(replaceStr)) {
            return uuid.replaceAll("-", "");
        }
        return uuid.replaceAll("-", replaceStr);
    }

    /**
     * 生成指定长度的UUID
     * 如果指定的长度为0，反复一个空字符串
     * 如果指定的长度为负数，抛出异常
     * @param num UUID长度
     * @param replaceStr 替换字符 有以下情况
     *                  1.null不替换  2.''，将'-'删除  3.其他字符，将'-'替换为该字符
     * @return 一个UUID字符串
     */
    public static String getUUID(int num, String replaceStr){
        String uuid = getUUID(replaceStr);
        // 如果长度为空，返回一个空字符串
        if(num == 0) {
            return "";
        }
        // 如果长度小于0，抛出异常
        if(num < 0) {
            throw  new IllegalArgumentException("请求的长度为负数");
        } else if(num >= uuid.length()) {
            // 如果长度超过生成的UUID长度，就返回这个UUID
            return uuid;
        }
        // 截取指定长度
        return uuid.substring(0, num);
    }


}
