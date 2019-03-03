package com.open.capacity.autoconfigure.log;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import com.alibaba.fastjson.JSONObject;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.log.service.LogService;
import com.open.capacity.log.service.impl.LogServiceImpl;
import com.open.capacity.model.log.SysLog;
import com.open.capacity.model.system.LoginAppUser;
import com.open.capacity.utils.SpringUtils;
import com.open.capacity.utils.SysUserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 保存日志
 * 
 * @author owen
 * @create 2017年7月2日
 */
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
@Slf4j
public class LogAnnotationAspect {
    @Around("@annotation(ds)")
    public Object logSave(ProceedingJoinPoint joinPoint, LogAnnotation ds) throws Throwable {

        SysLog sysLog = new SysLog();
        sysLog.setCreateTime(new Date());
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        if (loginAppUser != null) {
            sysLog.setUsername(loginAppUser.getUsername());
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
        sysLog.setModule(logAnnotation.module());

        if (logAnnotation.recordRequestParam()) {
            String[] paramNames = methodSignature.getParameterNames();// 参数名
            if (paramNames != null && paramNames.length > 0) {
                Object[] args = joinPoint.getArgs();// 参数值

                Map<String, Object> params = new HashMap<>();
                for (int i = 0; i < paramNames.length; i++) {

                    if (paramNames[i] instanceof Serializable) {
                        params.put(paramNames[i], args[i]);
                    }

                }

                try {
                    sysLog.setParams(JSONObject.toJSONString(params));
                } catch (Exception e) {
                    log.error("记录参数失败：{}", e.getMessage());
                }
            }
        }

        try {
            // 调用原来的方法
            Object object = joinPoint.proceed();
            sysLog.setFlag(Boolean.TRUE);

            return object;
        } catch (Exception e) {
            sysLog.setFlag(Boolean.FALSE);
            sysLog.setRemark(e.getMessage());

            throw e;
        } finally {

            CompletableFuture.runAsync(() -> {
                try {
                    LogService logService = SpringUtils.getBean(LogServiceImpl.class);
                    logService.save(sysLog);
                } catch (Exception e) {
                    log.error("记录参数失败：{}", e.getMessage());
                }
            });
        }
    }
}