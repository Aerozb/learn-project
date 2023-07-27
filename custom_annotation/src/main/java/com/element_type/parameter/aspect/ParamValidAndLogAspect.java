package com.element_type.parameter.aspect;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ParamValidAndLogAspect {

    @Pointcut("@annotation(com.element_type.parameter.annotation.ParamValidAndLog) && args(com.element_type.parameter.annotation.ParamValidAndLog)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object run(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("arg:{}", JSONUtil.toJsonStr(arg));
        }
        return null;
    }

}
