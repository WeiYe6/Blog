package com.fengye.service;


import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.User;

/**
 * @author fegnye
 */
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}