package cn.hankchan.utils;

/**
 * @author hankchan
 * @since 2018/9/1
 */
public class HexUtils {

    /**
     * 二进制转为大写的十六进制字符串
     * @param bytes 字节数组
     * @return 大写的十六进制字符串
     */
    public static String parseByte2UppercaseHexString(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
