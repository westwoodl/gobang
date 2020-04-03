package com.xrc.gb.repository.cache;

import com.xrc.gb.consts.ErrorInfoConstants;
import com.xrc.gb.exception.BusinessException;
import com.xrc.gb.util.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author xu rongchao
 * @date 2020/4/2 22:01
 */
@Slf4j
@Component
@Aspect
public class RedisAspect {
    @Pointcut("execution(* com.xrc.gb.repository.cache..*.*(..))")
    public void cacheAspect() {}

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = null;
        try {
            obj = pjp.proceed();
        }  catch (Throwable e) {
            log.info("CacheError{}", e.getMessage());
            throw e;
        }
        return obj;
    }
}
