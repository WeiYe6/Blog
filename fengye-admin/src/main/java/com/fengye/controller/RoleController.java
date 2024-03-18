package com.fengye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengye.constants.SystemConstants;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.ChangeRoleStatusDto;
import com.fengye.domain.pojo.Role;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.RoleService;
import com.fengye.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private RoleService roleService;


    //分页查询角色
    @GetMapping("/list")
    public ResponseResult getRolePageList(Integer pageNum, Integer pageSize, Role role){
        PageVO pageVO = roleService.getRolePageList(pageNum, pageSize, role);
        return ResponseResult.okResult(pageVO);
    }

    //新增角色
    @PostMapping("")
    public ResponseResult addRole(@RequestBody Role role){
        if (role == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        roleService.addRole(role);
        return ResponseResult.okResult();
    }

    //修改角色
    //根据id查询角色
    @GetMapping("/{roleId}")
    public ResponseResult getRoleById(@PathVariable("roleId") Long roleId){
        if (roleId == null || roleId <= 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        Role role = roleService.getById(roleId);
        return ResponseResult.okResult(role);
    }
    @PutMapping("")
    public ResponseResult updateRole(@RequestBody Role role){
        if (role == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
       roleService.updateRole(role);
        return ResponseResult.okResult();
    }

    //删除角色
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id) {
        if (id == null || id < 0) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        boolean b = roleService.removeById(id);
        if (!b) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        return ResponseResult.okResult();
    }

    //修改角色的停启用状态
    @PutMapping("/changeStatus")
    public ResponseResult updateRoleStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        if (changeRoleStatusDto == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        Role role = new Role();
        role.setId(changeRoleStatusDto.getRoleId());
        role.setStatus(changeRoleStatusDto.getStatus());

        Long roleId = role.getId();
        if (roleId == null || roleId < 0){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        String status = role.getStatus();
        if (StringUtils.isBlank(status)){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId, roleId);
        boolean b = roleService.updateById(role);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    //查询角色列表-用于后台新增用户的列表展示
    @GetMapping("listAllRole")
    public ResponseResult listAllRole(){
        //查询的是所有状态正常的角色
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = roleService.list();
        return ResponseResult.okResult(list);
    }
}