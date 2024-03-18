package com.fengye.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengye.domain.pojo.Menu;

import java.util.List;

/**
* @author 曾伟业
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2024-02-02 22:46:39
* @Entity generator.domain.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     *（非管理员）根据userId 查询user_role表的role_id，根据role_id查询role_menu表中的menu_id，进而查询perms字
     * @param userId 当前用户id
     * @return 返回结果
     */
    List<String> selectPermsByUserId(Long userId);

    /**
     * 管理员 返回所有符合要求的权限菜单
     * @return 返回
     */
    List<Menu> selectAllRouterMenu();

    /**
     * 查询对应用户所具有的路由信息(权限菜单)
     * @param userId 用户id
     * @return 返回
     */
    List<Menu> selectOtherRouterMenuTreeByUserId(Long userId);
}




