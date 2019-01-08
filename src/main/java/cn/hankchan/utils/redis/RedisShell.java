package cn.hankchan.utils.redis;

/**
 * @author hankChan
 */
public class RedisShell {

    public static final String REDIS_UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    public static final String REDIS_LIMIT_SCRIPT = "local notexists = redis.call(\"set\", KEYS[1], 1, \"NX\", \"EX\", tonumber(ARGV[2])) "
            + "if (notexists) then return 1 end local current = tonumber(redis.call(\"get\", KEYS[1])) "
            + "if (current == nil) then local result = redis.call(\"incr\", KEYS[1]) redis.call(\"expire\", KEYS[1], tonumber(ARGV[2])) return result end "
            + "if (current >= tonumber(ARGV[1])) then return 0 end local result = redis.call(\"incr\", KEYS[1]) return result";
}
