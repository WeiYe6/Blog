package com.fengye.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.domain.pojo.Role;
import com.fengye.domain.pojo.RoleMenu;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.mapper.RoleMapper;
import com.fengye.service.RoleMenuService;
import com.fengye.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 曾伟业
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2024-02-02 22:50:25
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        //如果是管理员 userId == 1 or 8，直接返回admin
        if (userId == 1 || userId == 8){
            List<String> roleKey = new ArrayList<>();
            roleKey.add("admin");
            return roleKey;
        }
        //非管理员，根据sql语句进行多表联查（提高效率）
        List<String> OrderRoleKey = getBaseMapper().selectRoleKeyByUserId(userId);
        return OrderRoleKey;
    }

    @Override
    public PageVO getRolePageList(Integer pageNum, Integer pageSize, Role role) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String roleName = role.getRoleName();
        if (StringUtils.isNoneBlank(roleName)) {
            roleLambdaQueryWrapper.like(Role::getRoleName, roleName);
        }
        String status = role.getStatus();
        if (StringUtils.isNoneBlank(status)) {
            roleLambdaQueryWrapper.eq(Role::getStatus, status);
        }
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        //分页查询
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        page(rolePage, roleLambdaQueryWrapper);

        //vo返回
        PageVO pageVO = new PageVO();
        pageVO.setRows(rolePage.getRecords());
        pageVO.setTotal(rolePage.getTotal());
        return pageVO;
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        //新增角色
        boolean b = this.save(role);
        if (!b) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        List<String> menuIds = role.getMenuIds();
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //新增对应的 角色和菜单关联表
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(menuId -> new RoleMenu(role.getId(), Long.valueOf(menuId)))
                .collect(Collectors.toList());
        //批量插入
        boolean b1 = roleMenuService.saveBatch(roleMenuList);
        if (!b1) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        //更新角色信息
        boolean b = this.updateById(role);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //更新角色的菜单权限列表信息
        List<String> menuIds = role.getMenuIds();
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1.删除该角色对应的菜单权限列表
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId, role.getId());
        boolean remove = roleMenuService.remove(roleMenuLambdaQueryWrapper);
        if (!remove){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //2.添加该角色对应的菜单权限列表
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(menuId -> new RoleMenu(role.getId(), Long.valueOf(menuId)))
                .collect(Collectors.toList());
        boolean b1 = roleMenuService.saveBatch(roleMenuList);
        if (!b1){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
}




