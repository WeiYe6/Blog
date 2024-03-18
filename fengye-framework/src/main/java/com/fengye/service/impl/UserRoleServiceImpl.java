package com.fengye.service.impl;

;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.domain.pojo.UserRole;
import com.fengye.mapper.UserRoleMapper;
import com.fengye.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
* @author 曾伟业
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2024-02-10 23:32:03
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

}




