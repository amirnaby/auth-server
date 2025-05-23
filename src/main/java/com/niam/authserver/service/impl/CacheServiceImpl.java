package com.niam.authserver.service.impl;

import com.niam.authserver.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
    private final CacheManager cacheManager;

    @Override
    public Cache getCache(String cacheName) {
        return cacheManager.getCache(cacheName);
    }
}