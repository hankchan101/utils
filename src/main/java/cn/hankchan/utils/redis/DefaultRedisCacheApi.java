package cn.hankchan.utils.redis;

import cn.hankchan.utils.RedisCacheApi;
import cn.hankchan.utils.Strings;

/**
 * @author hankChan
 */
public class DefaultRedisCacheApi implements RedisCacheApi {

    private RedisPool redisPool;

    public DefaultRedisCacheApi(RedisPool redisPool) {
        this.redisPool = redisPool;
    }

    @Override
    public String getString(String key) {
        if(Strings.isNullOrEmpty(key)) {
            return null;
        }
        return redisPool.get(key);
    }

    @Override
    public boolean deleteString(String key) {
        if(Strings.isNullOrEmpty(key)) {
            return false;
        }
        return redisPool.del(key) == 1;
    }

    @Override
    public boolean setString(String key, String value, int expireSeconds) {
        if(Strings.isNullOrEmpty(key) || expireSeconds <= 0) {
            return false;
        }
        return "OK".equals(redisPool.setex(key, value, expireSeconds));
    }
}
