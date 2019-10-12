package org.geekbeacon.social.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {


    @Override
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("socialData");
        cacheManager.setCaffeine(caffeineDefaultCache());
        return cacheManager;
    }

    @Bean
    public CacheManager durableCache() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("durable");
        cacheManager.setCaffeine(caffeineLongLived());
        return cacheManager;
    }


    private Caffeine<Object, Object> caffeineDefaultCache() {
        return Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(500)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .weakKeys()
            .recordStats();
    }

    private Caffeine<Object, Object> caffeineLongLived() {
        return Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(500)
            .expireAfterAccess(6, TimeUnit.HOURS)
            .weakKeys()
            .recordStats();
    }
}
