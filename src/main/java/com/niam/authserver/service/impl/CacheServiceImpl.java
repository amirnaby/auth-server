package com.niam.authserver.service.impl;

import com.niam.authserver.service.CacheService;
import com.niam.authserver.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
    private final CacheManager cacheManager;
    private final MessageUtil messageSource;

    @Override
    public Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        Assert.notNull(cache, messageSource.getMessage("message.notAccessibleCache", cacheName));
        return cache;
    }
}