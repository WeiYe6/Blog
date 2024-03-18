package com.fengye.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.User;
import com.fengye.domain.vo.GetUserRoleByIdVO;
import com.fengye.domain.vo.PageVO;

/**
* @author 曾伟业
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2024-01-31 08:56:34
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param user 注册的用户
     * @return 是否成功
     */
    ResponseResult register(User user);

    /**
     * 获取个人信息
     * @return 是否成功
     */
    ResponseResult getUserInfo();

    /**
     * 更新个人信息
     * @param user 更新的用户
     * @return 是否成功
     */
    ResponseResult updateUserInfo(User user);

    /**
     * 后台分页查询用户列表
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param user 模糊匹配信息
     * @return 返回用户列表
     */
    PageVO getUserPageList(Integer pageNum, Integer pageSize, User user);

    /**
     * 根据id查询用户，以及用户所关联的角色id列表
     * @param userId 用户id
     * @return 返回
     */
    GetUserRoleByIdVO getUserRoleById(Long userId);

    /**
     * 修改用户，以及更新用户所关联的用户角色
     * @param user
     */
    void updateUser(User user);
}
