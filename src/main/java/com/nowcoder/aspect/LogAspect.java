package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Chen on 03/05/2017.
 */
@Component
@Aspect
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuffer sbf = new StringBuffer();
        for (Object arg: joinPoint.getArgs()) {
            sbf.append("arg:" + arg.toString() + " | ");
        }
        logger.info("before method: " + sbf.toString());
    }

    @After("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void afterMethod() {
        logger.info("after method: ");
    }
}
