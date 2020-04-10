package com.xrc.gb.repository.cache;

import com.xrc.gb.common.util.Defaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * redis内部处理异常，有异常返回默认值
 *
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
        try {
            return pjp.proceed();
        }  catch (Throwable e) {
            MethodSignature signature = (MethodSignature)  pjp.getSignature();
            //获取method对象
            Method method = signature.getMethod();
            Class returnType = method.getReturnType();
            log.error("缓存异常, method {}:{}", method.getName(), e.getMessage());
            return Defaults.defaultValue(returnType);
        }
    }
}
