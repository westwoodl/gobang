package com.xrc.gb.service;

import com.xrc.gb.consts.ErrorInfoConstants;
import com.xrc.gb.util.ExceptionHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author xu rongchao
 * @date 2020/3/27 17:56
 */
@Component
@Aspect
public class ServiceAspect {

    @Pointcut("execution(* com.xrc.gb.service..*.*(..))")
    public void serviceAspect() {}

    @Around("serviceAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorInfoConstants.BIZ_PARAMETER_ERROR + e.getMessage());
        } catch (RuntimeException e) {
            throw ExceptionHelper.newBizException(ErrorInfoConstants.BIZ_SYSTEM_BUSY, e);
        }
        return obj;
    }
}
