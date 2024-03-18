package com.fengye.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fengye.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 配置 实体类字段的自动填充处理器
 * @author fengye
 * //这个类是用来配置mybatis字段的自动填充处理器。
 * 比如：在执行'发送评论'功能时，由于我们在评论表无法对下面这四个字段进行插入数据(原因是前端在发送评论时，没有在
 * 请求体提供下面四个参数，所以后端在往数据库插入数据时，下面四个字段是空值)，所有就需要这个类来帮助我们在执行什么操作语句时自动填充字段
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    //（插入填充）只要对数据库执行了插入语句，那么就会执行到这个方法
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        try {
            //获取当前登录用户id
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId = -1L;//如果异常了，就说明该用户还没注册，我们就把该用户的userId字段赋值d为-1
        }
        //自动填充下面四个字段的值。
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy",userId , metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    //（更新填充）
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName(" ", SecurityUtils.getUserId(), metaObject);
    }
}