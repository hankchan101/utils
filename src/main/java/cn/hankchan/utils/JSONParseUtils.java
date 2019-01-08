package cn.hankchan.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * JSON转换工具类
 * @author hankChan
 *         2018/7/5 0005.
 */
public class JSONParseUtils {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();

    /**
     * 将对象转换为JSON格式的字符串
     * @param obj 目标对象
     * @return 返回JSON格式字符串，或者null
     */
    public static String object2JsonString(Object obj) {
        if(obj == null) {
            return null;
        }
        return gson.toJson(obj);
    }

    /**
     * 将JSON字符串还原为目标对象
     * @param jsonString JSON字符串
     * @param resultType 目标对象类型
     * @param <T> 目标对象类型
     * @return 成功则目标对象，否则返回null
     */
    public static <T> T json2Object(String jsonString, Class<T> resultType) {
        if(Strings.isNullOrEmpty(jsonString)) {
            return null;
        }
        return gson.fromJson(jsonString, resultType);
    }

    /**
     * 将JSON字符串还原为泛型的目标对象
     * @param jsonString JSON字符串
     * @param typeToken 泛型目标对象包装类型
     * @param <T> 泛型
     * @return 结果对象
     */
    public static <T> T json2GenericObject(String jsonString,TypeToken<T> typeToken) {
        return gson.fromJson(jsonString, typeToken.getType());
    }
}
