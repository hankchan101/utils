package cn.hankchan.utils;

/**
 * Redis缓存服务接口
 * @author hankChan
 */
public interface RedisCacheApi {

    /**
     * 获取缓存
     * @param key 缓存key
     * @return 缓存值
     */
    String getString(String key);

    /**
     * 删除缓存
     * @param key 缓存key
     * @return 是否删除成功
     */
    boolean deleteString(String key);

    /**
     * 新增缓存
     * @param key 缓存key
     * @param value 缓存值
     * @param expireSeconds 缓存时效,单位:秒
     * @return 是否新增成功
     */
    boolean setString(String key, String value, int expireSeconds);
}
