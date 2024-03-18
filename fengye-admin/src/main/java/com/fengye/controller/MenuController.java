package com.fengye.controller;


import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.Menu;
import com.fengye.domain.pojo.Role;
import com.fengye.domain.vo.MenuTreeVo;
import com.fengye.domain.vo.MenuVo;
import com.fengye.domain.vo.UpdateMenuRoleVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.MenuService;
import com.fengye.utils.SystemConverter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    private MenuService menuService;



    //查询菜单：展示菜单列表，不需要分页
    @GetMapping("list")
    public ResponseResult menuList(Menu menu){
        List<Menu> menus  = menuService.MenuList(menu);
        return ResponseResult.okResult(menus);
    }

    //新增菜单
    @PostMapping("")
    public ResponseResult addMenu(@RequestBody Menu menu){
        if (menu == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
       menuService.addMenu(menu);
        return ResponseResult.okResult();
    }

    //修改菜单
    //根据id查询菜单
    @GetMapping("{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        if (id == null || id <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        Menu menu = menuService.getById(id);
        if (menu == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult(menu);
    }
    @PutMapping("")
    public ResponseResult updateArticle(@RequestBody Menu menu){
        if (menu == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        if (menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        boolean b = menuService.updateById(menu);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    //删除菜单
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        if (id == null || id < 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        boolean b = menuService.removeById(id);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    //获取菜单下拉树列表
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        //复用之前 展示菜单列表的方法
        List<Menu> menus = menuService.MenuList(new Menu());
        //构建下拉菜单树列表
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(menuTreeVos);
    }

    //根据id查询 对应的角色菜单列表树
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getRoleMenuTreeById(@PathVariable("id") Long roleId){
        if (roleId == null || roleId <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        //获取所有的下拉菜单树列表
        List<Menu> menus = menuService.MenuList(new Menu());
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        UpdateMenuRoleVO updateMenuRoleVO = new UpdateMenuRoleVO();
        updateMenuRoleVO.setMenus(menuTreeVos);

        //获取该角色所关联的菜单权限id列表
        List<Long> roleMenuTreeById = menuService.getRoleMenuTreeById(roleId);
        updateMenuRoleVO.setCheckedKeys(roleMenuTreeById);

        return ResponseResult.okResult(updateMenuRoleVO);
    }
}