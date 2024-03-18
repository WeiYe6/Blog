package com.fengye.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.constants.SystemConstants;
import com.fengye.domain.pojo.Menu;
import com.fengye.domain.pojo.RoleMenu;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.mapper.MenuMapper;
import com.fengye.service.MenuService;
import com.fengye.service.RoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 曾伟业
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2024-02-02 22:46:39
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService {

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        //（管理员 id = 1 or 8） 直接赋值全部权限
        if (userId == 1L || userId == 8){
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //查询条件是permissions中需要有所有菜单类型为C或者F的权限。
            menuLambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.TYPE_MENU, SystemConstants.TYPE_BUTTON);
            //查询条件是permissions中需要有状态为正常的权限。
            menuLambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = this.list(menuLambdaQueryWrapper);
            //获取perms字段
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //为了提高查询效率-采用编写sql语句来进行多表联查
        //（非管理员）根据userId 查询user_role表的role_id，根据role_id查询role_menu表中的menu_id，进而查询perms字段
        List<String> OrderPerms = getBaseMapper().selectPermsByUserId(userId);
        return OrderPerms;
    }


    //----------------------------------查询用户的路由信息(权限菜单)-------------------------------------
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是超级管理员，用户id为1 or 8 管理员，就返回所有符合要求的权限菜单
        //用户为管理员，menus中需要有所有菜单类型为C或者M的，C表示菜单，M表示目录，状态为正常的，未被删除的权限
        if (userId.equals(1L) || userId.equals(8L)){
            menus = menuMapper.selectAllRouterMenu();
        }else {
            //如果不是超级管理员，就查询对应用户所具有的路由信息(权限菜单)
            menus = menuMapper.selectOtherRouterMenuTreeByUserId(userId);
        }

        //构建成tree，也就是子父菜单树，有层级关系
        //思路:先找出第一层的菜单，然后再找子菜单(也就是第二层)，把子菜单的结果赋值给Menu类的children字段
        List<Menu> menuTree = buildMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public List<Menu> MenuList(Menu menu) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String menuName = menu.getMenuName();
        if (StringUtils.isNoneBlank(menuName)) {
            menuLambdaQueryWrapper.like(Menu::getMenuName, menuName);
        }
        String status = menu.getStatus();
        if (StringUtils.isNoneBlank(status)) {
            menuLambdaQueryWrapper.eq(Menu::getStatus, status);
        }
        //按照父菜单id和orderNum进行排序
        menuLambdaQueryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menus = this.list(menuLambdaQueryWrapper);
        return menus;
    }

    @Override
    public void addMenu(Menu menu) {
        boolean b = this.save(menu);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public List<Long> getRoleMenuTreeById(Long roleId) {
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<RoleMenu> roleMenus = null;
        //当用户为超级管理员时 即roleId == 1 or 8 时赋予全部权限
        if (roleId == 1 || roleId == 8){
            roleMenus = roleMenuService.list();

        }else {
            roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId, roleId);
             roleMenus = roleMenuService.list(roleMenuLambdaQueryWrapper);
        }
        List<Long> longs = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        return longs;
    }


    /**
     * 构建菜单的层级关系（对当前菜单的 children属性赋值）
     * @param menus   需要构建的菜单列表
     * @param parentId 父菜单id
     * @return 返回构建好的菜单列表
     */
    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {
        return menus.stream()
                //过滤找出父菜单树，也就是第一层
                .filter(menu -> menu.getParentId().equals(parentId))
                //getChildren是我们在下面写的方法，用于获取子菜单的List集合
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
    }

    /**
     * 用于获取传入参数的子菜单，并封装为List集合返回
     * @param menu  为父菜单
     * @param menus 子菜单列表
     * @return 返回该菜单的子菜单列表
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        return menus.stream()
                //过滤，获取当前菜单的子菜单
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三层菜单的话，也就是子菜单的子菜单，我们就用下面那行递归(自己调用自己)来处理
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }
}




