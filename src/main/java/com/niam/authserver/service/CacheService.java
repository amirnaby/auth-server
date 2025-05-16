package com.niam.authserver.service;

import org.springframework.cache.Cache;

public interface CacheService {

    Cache getCache(String cacheName);
}
