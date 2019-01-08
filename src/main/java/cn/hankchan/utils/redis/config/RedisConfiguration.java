package cn.hankchan.utils.redis.config;

import cn.hankchan.utils.RedisCacheApi;
import cn.hankchan.utils.redis.DefaultRedisCacheApi;
import cn.hankchan.utils.redis.RedisPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hankChan
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisCacheApi redisCacheApi() {
        return new DefaultRedisCacheApi(redisPool());
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public RedisPool redisPool() {
        return new RedisPool();
    }
}
