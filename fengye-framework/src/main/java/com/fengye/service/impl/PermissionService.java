package com.fengye.service.impl;


import com.fengye.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SpringSecurity 自定义权限校验的代码逻辑实现
 * @author fengye
 */
@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是不是具有permission的权限
     * @param permission 该接口需要的权限关键词
     * @return 如果满足返回true  反之返回false
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员，直接放行返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }

        //对当前登录用户进行权限校验，判断他的权限是否满足permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
