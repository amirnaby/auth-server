package com.niam.authserver.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.niam.authserver.aop.annotation.FiltersPermission;
import com.niam.authserver.config.acl.AclPermissionHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
public class FiltersPermissionAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(FiltersPermissionAspect.class);
    private final AclPermissionHandler permissionHandler;
    private final ObjectMapper mapper;

    @PostConstruct
    public void setup() {
        mapper.registerModule(new Jdk8Module());
    }

    @Before("@annotation(filtersPermission)")
    public void beforeFiltersPermission(JoinPoint joinPoint, FiltersPermission filtersPermission) {
        LOGGER.info(joinPoint.getSignature().toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> permittedValues = permissionHandler.getListOfPermissionNamesByType(authentication,
                filtersPermission.targetType().getTypeName(),
                filtersPermission.permission());
        Map<String, Object> filter = getFilterFromArgs(joinPoint.getArgs());
        String filterKey = filtersPermission.value();

        if (filter.containsKey(filterKey)) {
            Set<String> filteredValues = collectValuesWithPermission(filter.get(filtersPermission.value()), permittedValues);
            if (!filteredValues.isEmpty()) {
                filter.put(filterKey, filteredValues);
            } else {
                filter.put(filterKey, permittedValues);
            }
        } else {
            filter.put(filterKey, permittedValues);
        }
    }

    private Set<String> collectValuesWithPermission(Object fromFilter, Set<String> permittedValues) {
        Set<String> result = new HashSet<>();
        if (fromFilter instanceof String) {
            if (permittedValues.contains(fromFilter)) result.add((String) fromFilter);
        } else if (fromFilter instanceof Collection<?>) {
            ((Collection<?>) fromFilter).forEach(s -> {
                if (permittedValues.contains(s)) result.add((String) s);
            });
        } else {
            throw new RuntimeException("Filter has unsupported type input: " + fromFilter);
        }
        return result;
    }

    private Map<String, Object> getFilterFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Map<?, ?>) return (Map<String, Object>) arg;
        }
        return new HashMap<>();
    }
}