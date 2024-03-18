package com.fengye.utils;

import com.fengye.domain.pojo.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SpringSecurity的工具封装类
 * @author fengye
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户
     **/
    public static LoginUser getLoginUser() {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication 认证
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断当前用户是否是超级管理员
     * 指定userId为1 或者 8的用户就是网站管理员
     * @return 是或否
     */
    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && (1L == id || 8L == id);
    }

    /**
     * 获取当前用户的id
     * @return
     */
    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}