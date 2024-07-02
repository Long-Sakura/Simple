package com.itxxy.aop;

import com.itxxy.mapper.UserMapper;
import com.itxxy.pojo.Log;
import com.itxxy.pojo.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.itxxy.constant.UserConstant.USER_LOGIN_STATE;

@Component
@Aspect
public class LogAspect {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Resource
    UserMapper userMapper;
    @Around("@annotation(com.itxxy.annotation.Log)")
    public Object logRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        User user =(User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        Long id = user.getId();
        String methodName = joinPoint.getSignature().getName();
        Log log=new Log(id,methodName,"一次日志记录");
        userMapper.logInsert(log);
        return result;
    }
}
