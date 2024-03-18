package com.fengye.exception;


import com.fengye.enums.AppHttpCodeEnum;

/**
 * 自定义异常 （提高系统的灵活性）
 * @author fengye
 */
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    //定义一个构造方法，接收的参数是枚举类型，AppHttpCodeEnum是我们在fengye-framework工程定义的枚举类
    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        //把某个枚举类里面的code和msg赋值给异常对象
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}