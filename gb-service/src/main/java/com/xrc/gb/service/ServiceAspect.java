package com.xrc.gb.service;

import com.xrc.gb.common.consts.ErrorInfoConstants;
import com.xrc.gb.common.exception.BusinessException;
import com.xrc.gb.common.util.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ServiceAspect {

    @Pointcut("execution(* com.xrc.gb.service..*.*(..))")
    public void serviceAspect() {}

    @Around("serviceAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (BusinessException b) {
            log.info("Service:business exception{}", b.getMessage());
            throw b;
        } catch (IllegalArgumentException e) {
            log.info("Service{}", e.getMessage());
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_PARAMETER_ERROR);
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Service Throwable{}", e.getMessage());
            throw ExceptionHelper.newSysException(ErrorInfoConstants.BIZ_SYSTEM_BUSY);
        }
        return obj;
    }
}
