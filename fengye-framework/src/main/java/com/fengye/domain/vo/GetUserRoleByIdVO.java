package com.fengye.domain.vo;

import com.fengye.domain.pojo.Role;
import com.fengye.domain.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 根据用户id，查询用户信息以及该用户关联的角色id 和 所有角色列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserRoleByIdVO {
    /**
     * 用户所关联的角色id列表
     */
    private List<Long> roleIds;

    /**
     * 所有角色列表
     */
    private List<Role> roles;

    /**
     * 该id用户
     */
    private User user;
}
