package cn.hankchan.utils;

/**
 * 字符串工具类
 * @author hankChan
 *
 */
public class Strings {

    /**
     * 是否全部不为null或空字符串
     * @param strs 目标字符串,可以是多个
     * @return 是否全部不为null或空字符串
     */
    public static boolean areNotNullOrEmpty(String ... strs) {
        if(strs == null || strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if(isNullOrEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为null或者空字符串
     * @param str 目标字符串
     * @return 是否为null或者空字符串
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 如果目标字符串为null,返回空字符串,否则返回它本身
     * @param str 目标字符串
     * @return 如果目标字符串为null,返回空字符串,否则返回它本身
     */
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * 如果目标字符串为空字符串,返回null,否则返回它本身
     * @param str 目标字符串
     * @return 如果目标字符串为空字符串,返回null,否则返回它本身
     */
    public static String emptyToNull(String str) {
        if(str == null) {
            return null;
        }
        return str.isEmpty() ? null : str;
    }

}
