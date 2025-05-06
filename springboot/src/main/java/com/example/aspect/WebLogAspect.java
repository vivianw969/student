package com.example.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.example.annotation.WebLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class WebLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("@annotation(com.example.annotation.WebLog)")
    public void webLog() {}

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 执行方法
        Object result = joinPoint.proceed();

        // 获取方法信息
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        WebLog webLogAnnotation = method.getAnnotation(WebLog.class);

        // 记录请求信息
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("description", webLogAnnotation.description());
        logMap.put("username", webLogAnnotation.username());
        logMap.put("startTime", startTime);
        logMap.put("spendTime", System.currentTimeMillis() - startTime);
        logMap.put("basePath", StrUtil.removeSuffix(request.getRequestURL().toString(), URLUtil.url(request.getRequestURL().toString()).getPath()));
        logMap.put("uri", request.getRequestURI());
        logMap.put("url", request.getRequestURL().toString());
        logMap.put("method", request.getMethod());
        logMap.put("ip", request.getRemoteAddr());
        logMap.put("parameter", JSONUtil.toJsonStr(joinPoint.getArgs()));
        logMap.put("result", JSONUtil.toJsonStr(result));

        // 打印日志
        LOGGER.info("{}", JSONUtil.parse(logMap));

        return result;
    }
}
