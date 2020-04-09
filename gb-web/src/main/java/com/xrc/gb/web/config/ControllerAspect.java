package com.xrc.gb.web.config;

import com.xrc.gb.util.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

/**
 * @author xu rongchao
 * @date 2020/3/3 21:10
 */

@Component
@Aspect
@Slf4j
public class ControllerAspect {


    @Pointcut("execution(public * com.xrc.gb.web.controller.*.* (..))")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (Throwable throwable) {
            log.error("Controller:erro{}", throwable.getMessage());
            throw throwable;
        }
        stopWatch.stop();

        log.info("ControllerAspect {}，{},{}，{} ms",
                pjp.getSignature().toShortString(), Arrays.toString(pjp.getArgs()), obj,
                stopWatch.getTotalTimeMillis());

        return obj;
    }


}
