package cn.hankchan.utils.consts;

/**
 * @author hankChan
 */
public class ApplicationConfigConst {

    public static final String REDIS_IP = "${hankchan.java.redis.ip}";

    public static final String REDIS_PORT = "${hankchan.java.redis.port}";

    public static final String REDIS_PASSWORD = "${hankchan.java.redis.password}";

    public static final String REDIS_TIMEOUT = "${hankchan.java.redis.timeout}";

    public static final String REDIS_MAX_TOTAL = "${hankchan.java.redis.max-total}";

    public static final String REDIS_MAX_IDLE = "${hankchan.java.redis.max-idle}";

    public static final String REDIS_MAX_WAIT_MILLIS = "${hankchan.java.redis.max-wait-millis}";

    public static final String REDIS_TEST_ON_BORROW = "${hankchan.java.redis.test-on-borrow}";

    public static final String REDIS_TEST_ON_RETURN = "${hankchan.java.redis.test-on-return}";

}
