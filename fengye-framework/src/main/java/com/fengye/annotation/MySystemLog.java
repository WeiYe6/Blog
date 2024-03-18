package com.fengye.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * '自定义注解'类 ：实现日记记录的打印
 *
 * @author fengye
 */
@Retention(RetentionPolicy.RUNTIME)//表示MySystemLog注解类会保持到runtime阶段
@Target(ElementType.METHOD) //表示MySystemLog注解类的注解功能只能用于方法上
public @interface MySystemLog {

    //为controller提供接口的描述信息，用于'日志记录'功能
    String businessName();
}