package com.egrevs.project.lanittest.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public CacheManager cacheManager(){
        CaffeineCache caffeineCache = new CaffeineCache(
                "statistics",
                Caffeine.newBuilder()
                        .maximumSize(1)
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .build()
        );
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(caffeineCache));
        return cacheManager;
    }
}
