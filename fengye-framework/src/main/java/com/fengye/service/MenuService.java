package com.fengye.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.pojo.Menu;
import com.fengye.domain.vo.MenuVo;

import java.util.List;

/**
* @author 曾伟业
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2024-02-02 22:46:39
*/
public interface MenuService extends IService<Menu> {

    /**
     * 根据用户id查询权限信息(menu表中的 perms字段)
     * @param userId 当前登录的userId
     * @return 返回
     */
    List<String> selectPermsByUserId(Long userId);

    /**
     * 根据用户id来查询menu(权限菜单)。要求查询结果是tree的形式，也就是子父菜单树
     * @param userId 当前登录用户Id
     * @return 返回
     */
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    /**
     * 展示菜单列表，不需要分页
     * @param menu 需要查询的字段信息
     * @return 返回
     */
    List<Menu> MenuList(Menu menu);

    /**
     * 新增菜单
     * @param menu 新增的菜单信息
     */
    void addMenu(Menu menu);

    /**
     * 获取该角色所关联的菜单权限id列表
     * @param roleId 角色id
     * @return 返回菜单权限id列表
     */
    List<Long> getRoleMenuTreeById(Long roleId);
}
