package com.fengye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.pojo.Role;
import com.fengye.domain.vo.PageVO;

import java.util.List;

/**
* @author 曾伟业
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2024-02-02 22:50:25
*/
public interface RoleService extends IService<Role> {

    /**
     * 根据userId查询当前用户的角色信息
     * @param userId 用户id
     * @return 返回
     */
    List<String> selectRoleKeyByUserId(Long userId);

    /**
     * 分页查询角色列表
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param role 模糊查询
     * @return 返回列表
     */
    PageVO getRolePageList(Integer pageNum, Integer pageSize, Role role);

    /**
     * 新增角色
     * @param role 角色
     */
    void addRole(Role role);

    /**
     * 修改角色
     * @param role 角色信息
     */
    void updateRole(Role role);
}
