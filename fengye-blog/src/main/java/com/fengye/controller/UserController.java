package com.fengye.controller;

import com.fengye.annotation.MySystemLog;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.User;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //用户注册
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        if (user == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        if (StringUtils.isAnyBlank(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USER_NAME_NULL_ERROR);
        }
        if (StringUtils.isAnyBlank(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.USER_NICKNAME_NULL_ERROR);
        }
        if (StringUtils.isAnyBlank(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.USER_PASSWORD_NULL_ERROR);
        }
        return userService.register(user);
    }

    //查询个人信息
    @GetMapping("/userInfo")
    @MySystemLog(businessName = "查询个人中心信息")
    public ResponseResult getUserInfo(){
        return userService.getUserInfo();
    }

    //更新个人信息
    @PutMapping("/userInfo")
    @MySystemLog(businessName = "更新个人信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        if (user == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return userService.updateUserInfo(user);
    }

}