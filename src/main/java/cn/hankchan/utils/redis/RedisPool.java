package cn.hankchan.utils.redis;

import cn.hankchan.utils.consts.ApplicationConfigConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;
import java.util.Optional;

/**
 * @author hankChan
 */
public class RedisPool {

    static final Logger LOGGER = LoggerFactory.getLogger(RedisPool.class);

    private static JedisPool jedisPool;

    @Value(ApplicationConfigConst.REDIS_IP)
    private String ip;

    @Value(ApplicationConfigConst.REDIS_PORT)
    private Integer port;

    @Value(ApplicationConfigConst.REDIS_PASSWORD)
    private String password;

    @Value(ApplicationConfigConst.REDIS_TIMEOUT)
    private Integer timeout;

    @Value(ApplicationConfigConst.REDIS_MAX_TOTAL)
    private Integer maxTotal;

    @Value(ApplicationConfigConst.REDIS_MAX_IDLE)
    private Integer maxIdle;

    @Value(ApplicationConfigConst.REDIS_MAX_WAIT_MILLIS)
    private Long maxWaitMillis;

    @Value(ApplicationConfigConst.REDIS_TEST_ON_BORROW)
    private Boolean testOnBorrow;

    @Value(ApplicationConfigConst.REDIS_TEST_ON_RETURN)
    private Boolean testOnReturn;

    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Optional.ofNullable(maxTotal).orElse(1024));
        config.setMaxIdle(Optional.ofNullable(maxIdle).orElse(1000));
        config.setMaxWaitMillis(Optional.ofNullable(maxWaitMillis).orElse(2000L));
        config.setTestOnBorrow(Optional.ofNullable(testOnBorrow).orElse(true));
        config.setTestOnReturn(Optional.ofNullable(testOnReturn).orElse(true));
        jedisPool = new JedisPool(config,
                Optional.ofNullable(ip).orElse("localhost"),
                Optional.ofNullable(port).orElse(6379),
                Optional.ofNullable(timeout).orElse(100000),
                Optional.ofNullable(password).orElse(""));
        if(isConnectSuccess()) {
            // 初始化成功
            LOGGER.info("------ Init Redis Pool SUCCESS! Redis IP={} Port={} ------", ip, port);
        } else {
            // 初始化失败
            LOGGER.info("------ Init Redis Pool FAILURE! Redis IP={} Port={} ------", ip, port);
        }
    }

    public boolean unlock(String key, String requestId) {
        Jedis jedis = getRedis();
        Object result = jedis.eval(RedisShell.REDIS_UNLOCK_SCRIPT, Collections.singletonList(key), Collections.singletonList(requestId));
        recycleRedis(jedis);
        return Long.valueOf(1L).equals(result);
    }

    public boolean lock(String key, String requestId, int expireMillionSeconds) {
        Jedis jedis = getRedis();
        String result = jedis.set(key, requestId, "NX", "PX", expireMillionSeconds);
        recycleRedis(jedis);
        return "OK".equals(result);
    }

    public boolean limit(String key, int maxAllowSize, int cycleSeconds) {
        Jedis jedis = getRedis();
        Object result = jedis.eval(RedisShell.REDIS_LIMIT_SCRIPT, 1, key, String.valueOf(maxAllowSize), String.valueOf(cycleSeconds));
        recycleRedis(jedis);
        return ((Long) result > 0) && ((Long) result <= maxAllowSize);
    }

    public long del(String key) {
        Jedis jedis = getRedis();
        long result = jedis.del(key);
        recycleRedis(jedis);
        return result;
    }

    public String get(String key) {
        Jedis jedis = getRedis();
        String result = jedis.get(key);
        recycleRedis(jedis);
        return result;
    }

    public String setex(String key, String value, int expireSeconds) {
        Jedis jedis = getRedis();
        String result = jedis.setex(key, expireSeconds, value);
        recycleRedis(jedis);
        return result;
    }

    /**
     * 重新初始化连接池
     */
    public void reload() {
        close();
        init();
    }

    /**
     * 销毁Redis连接池
     */
    public void close() {
        if(null != jedisPool) {
            jedisPool.close();
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 资源回收
     * @param jedis jedis实例
     */
    private void recycleRedis(Jedis jedis) {
        jedis.close();
    }

    /**
     * @return 连接池中的Jedis实例
     */
    private Jedis getRedis() {
        return jedisPool.getResource();
    }

    /**
     * @return 是否成功连接上redis
     */
    private boolean isConnectSuccess() {
        Jedis jedis = getRedis();
        String ping = jedis.ping();
        recycleRedis(jedis);
        return ping.equals("PONG");
    }

}
