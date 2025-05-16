package com.niam.authserver.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class RestLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.behsa.smspanel.*.controller.*.*(..))")
    public Object logRestControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] methodArgs = joinPoint.getArgs();

        logger.info("Executing {}.{}() with arguments: {}", className, methodName, Arrays.toString(methodArgs));

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        logger.info("{}.{}() executed in {} ms with result: {}", className, methodName, endTime - startTime, result);

        return result;
    }
}