package com.fengye.controller;

import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.LoginUser;
import com.fengye.domain.pojo.Menu;
import com.fengye.domain.pojo.User;
import com.fengye.domain.vo.AdminUserInfoVo;
import com.fengye.domain.vo.RoutersVO;
import com.fengye.domain.vo.UserInfoVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.LoginService;
import com.fengye.service.MenuService;
import com.fengye.service.RoleService;
import com.fengye.utils.BeanCopyUtils;
import com.fengye.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fengye
 */
@RestController
public class LoginController {

    @Resource
    //LoginService是我们在service目录写的接口
    private LoginService loginService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;

    //使用SpringSecurity登录
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (user == null){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (StringUtils.isBlank(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }


    //查询管理员和普通用户的权限 和 角色信息
    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        //获取当前登录的用户
        User user = SecurityUtils.getLoginUser().getUser();
        //根据用户id查询权限信息(menu表中的 perms字段)
        Long userId = user.getId();
        if (userId == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        List<String>  permissions = menuService.selectPermsByUserId(userId);

        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(userId);

        //获取用户信息，并以userInfo的形式返回
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);

        //以AdminUserInfoVo封装返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permissions, roleKeyList, userInfoVO);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    //动态路由，实现不同用户 返回所具备的菜单数据
    //注意: 返回的菜单数据需要体现父子菜单的层级关系
    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        //获取userId
        Long userId = SecurityUtils.getUserId();
        if (userId == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //根据用户id来查询menu(权限菜单)。要求查询结果是tree的形式，也就是子父菜单树
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装响应返回
        return ResponseResult.okResult(new RoutersVO(menus));
    }

}