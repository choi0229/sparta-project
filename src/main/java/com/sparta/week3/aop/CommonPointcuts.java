package com.sparta.week3.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointcuts {

    @Pointcut("@annotation(com.sparta.week3.common.annotation.Loggable)")
    public void loggableMethods(){
    }
}
