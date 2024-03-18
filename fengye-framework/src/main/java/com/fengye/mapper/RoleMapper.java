package com.fengye.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengye.domain.pojo.Role;

import java.util.List;

/**
* @author 曾伟业
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2024-02-02 22:50:25
* @Entity generator.domain.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    /**
     *非管理员，根据sql语句进行多表联查（提高效率）
     * @param userId 当前用户id
     * @return 返回结果
     */
    List<String> selectRoleKeyByUserId(Long userId);
}




