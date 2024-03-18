package com.fengye.controller;

import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.User;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.BlogLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
public class BlogLoginController {

    @Resource
    //BlogLoginService是我们在service目录写的接口
    private BlogLoginService blogLoginService;

    //使用SpringSecurity登录
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if (user == null){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (StringUtils.isBlank(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }

}