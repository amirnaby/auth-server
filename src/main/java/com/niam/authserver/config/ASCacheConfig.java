package com.niam.authserver.config;

import com.niam.authserver.utils.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ASCacheConfig {
    private final CacheManager cacheManager;

    @Bean
    @Primary
    public CacheManager customCacheManager() {
        SimpleCacheManager customCacheManager = new SimpleCacheManager();
        Collection<? extends Cache> existingCaches = cacheManager.getCacheNames().stream()
                .map(cacheManager::getCache)
                .toList();

        List<Cache> caches = Arrays.stream(CacheRepository.class.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isPublic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers()) &&
                        field.getType().equals(String.class))
                .map(field -> {
                    try {
                        return (String) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Unable to access cache field", e);
                    }
                })
                .map(ConcurrentMapCache::new)
                .collect(Collectors.toList());

        List<Cache> allCaches = new ArrayList<>(existingCaches);
        allCaches.addAll(caches);
        customCacheManager.setCaches(allCaches);

        return customCacheManager;
    }
}