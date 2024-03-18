package com.fengye.aspect;

import com.alibaba.fastjson.JSON;
import com.fengye.annotation.MySystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/***
 * Aop切面类：执行自定义注解对日记记录的增强
 */
@Aspect //告诉spring容器，MyLogAspect是切面类
@Slf4j //打印日记
@Component
public class MyLogAspect {

    //定义切点: 确定哪个切点，以后哪个类想成为切点，就在对应的类上添加 自定义的注解。
    @Pointcut("@annotation(com.fengye.annotation.MySystemLog)")
    public void pt() {
    }

    //定义通知方法(这里用的是环绕通知): 通知方法也就是增强的具体逻辑代码。
    @Around("pt()")
    //ProceedingJoinPoint可以拿到被增强方法的信息
    //joinPoint.proceed方法表示调用目标方法，ret就是目标方法执行完之后的返回值
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        try {
            //方法执行前增强：调用'实现打印日志信息的格式信息'的方法
            handleBefore(joinPoint);
            ret = joinPoint.proceed();//目标方法执行完成，上一行是目标方法未执行，下一行是目标方法已经执行
            //方法执行后增强：调用'实现打印日志信息的数据信息'的方法
            handleAfter(ret);
        } finally {
            //下面的语句，不管有没有出异常都会被执行。
            //System.lineSeparator表示当前系统的换行符
            log.info("=======================end=======================" + System.lineSeparator());
        }

        //封装成切面，然后返回
        return ret;
    }


    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //ServletRequestAttributes是 RequestAttributes的接口实现类
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //下面那行就可以拿到请求的报文了，其中有我们需要的url、请求方式、ip。
        HttpServletRequest request = null;
        if (requestAttributes != null) {
            request = requestAttributes.getRequest();
        }
        //获取被增强方法的注解对象，例如获取UserController类的updateUserInfo方法上一行的@MySystemLog注解
        MySystemLog systemLog = getSystemLog(joinPoint);

        log.info("======================Start======================");//下面的几个log输出，第一个参数{}表示占位符，具体的值是第二个参数的变量
        // 打印请求 URL
        if (request != null) {
            log.info("请求URL   : {}", request.getRequestURL());
        }
        // 打印描述信息，例如获取UserController类的updateUserInfo方法上一行的@mySystemlog注解的描述信息
        log.info("接口描述   : {}", systemLog.businessName());
        // 打印 Http method
        if (request != null) {
            log.info("请求方式   : {}", request.getMethod());
        }
        // 打印调用 controller 的全路径(全类名)、方法名
        log.info("请求类名   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        if (request != null) {
            log.info("访问IP    : {}", request.getRemoteHost());
        }
        // 打印请求入参。JSON.toJSONString十FastJson提供的工具方法，能把数组转成JSON
        log.info("传入参数   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    //获取被增强方法的注解对象，例如获取UserController类的updateUserInfo方法上一行的@MySystemLog注解
    private MySystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        //Signature是spring提供的接口，MethodSignature是Signature的子接口
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //MySystemLog是我们写的自定义注解的接口
        //下面那行就能获取被增强方法的注解对象，例如获取UserController类的updateUserInfo方法上一行的@mySystemlog注解
        return methodSignature.getMethod().getAnnotation(MySystemLog.class);
    }

    private void handleAfter(Object ret) {
        //打印出参 以JSON打印
        log.info("返回参数   : {}", JSON.toJSONString(ret));
    }

}
