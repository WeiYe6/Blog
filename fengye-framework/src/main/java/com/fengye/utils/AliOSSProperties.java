package com.fengye.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//创建实体类目的：为了将配置信息yml中的 属性值 自动注入进来 而不用一个一个注解的去写（臃肿）
@Data //添加get、set... 除了构造函数之外的方法
@Component //添加进IOC容器管理
@ConfigurationProperties(prefix = "aliyun.oss") //指定前缀，来完成属性的自动注入
public class AliOSSProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
