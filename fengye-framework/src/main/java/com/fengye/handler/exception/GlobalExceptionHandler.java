package com.fengye.handler.exception;


import com.fengye.domain.ResponseResult;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器 （全局异常处理：最终都会在这个类进行处理异常）
 * @author fengye
 */
@RestControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

    //SystemException是我的自定义异常类。
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){

        //打印异常信息，方便我们追溯问题的原因。{}是占位符，具体值由e决定
        log.error("出现了异常! {}",e);

        //从异常对象中获取提示信息封装，然后返回。ResponseResult是我们写的类
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    //其它异常交给Exception
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){

        //打印异常信息，方便我们追溯问题的原因。{}是占位符，具体值由e决定
        log.error("出现了异常! {}",e);

        //从异常对象中获取提示信息封装，然后返回。ResponseResult、AppHttpCodeEnum是我们写的类
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());//枚举值是500
    }
}