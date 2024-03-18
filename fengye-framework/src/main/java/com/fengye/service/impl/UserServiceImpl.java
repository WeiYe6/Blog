package com.fengye.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.Role;
import com.fengye.domain.pojo.User;
import com.fengye.domain.pojo.UserRole;
import com.fengye.domain.vo.GetUserRoleByIdVO;
import com.fengye.domain.vo.PageVO;
import com.fengye.domain.vo.UserInfoVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.mapper.UserMapper;
import com.fengye.service.RoleService;
import com.fengye.service.UserRoleService;
import com.fengye.service.UserService;
import com.fengye.utils.BeanCopyUtils;
import com.fengye.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 曾伟业
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-01-31 08:56:34
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;

    //注册
    @Override
    @Transactional
    public ResponseResult register(User user) {
        //判断用户名是否存在（唯一性）
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        // 创建一个BCryptPasswordEncoder对象，使用默认的加密强度
        //参数是传递给BCryptPasswordEncoder的加密强度值位于合法范围内（4到31之间）
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //对用户密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        //讲加密后的密码设置回用户对象
        user.setPassword(encodedPassword);

        boolean b = this.save(user);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        //后台添加用户时，一同关联用户的角色权限信息
        Long[] roleIds = user.getRoleIds();
        if (!CollectionUtils.isEmpty(Arrays.asList(roleIds))){
            //添加用户角色信息
            List<UserRole> userRoleList = Arrays.stream(roleIds)
                    .map(roleId -> new UserRole(user.getId(), roleId))
                    .collect(Collectors.toList());
            boolean b1 = userRoleService.saveBatch(userRoleList);
            if (!b1){
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
            }
        }
        return ResponseResult.okResult();
    }

    //判断用户名是否存在
    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, userName);
        int count = this.count(userLambdaQueryWrapper);
        return count > 0; //用户名存在，返回true
    }

    @Override
    public ResponseResult getUserInfo() {
        //获取当前登录用户id
        Long userId = SecurityUtils.getUserId();
        if (userId <= 0){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //查询当前用户信息
        User user = this.getById(userId);
        if (user == null){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //封装成userInfoVO返回
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);
        return ResponseResult.okResult(userInfoVO);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        if (user == null){
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }

        boolean b = this.updateById(user);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        return ResponseResult.okResult();
    }

    @Override
    public PageVO getUserPageList(Integer pageNum, Integer pageSize, User user) {
        //分页查询用户
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据用户名模糊查询
        String userName = user.getUserName();
        if (StringUtils.isNoneBlank(userName)) {
            userLambdaQueryWrapper.like(User::getUserName, userName);
        }
        //精确手机号查询
        String phonenumber = user.getPhonenumber();
        if (StringUtils.isNoneBlank(phonenumber)) {
            userLambdaQueryWrapper.eq(User::getPhonenumber, phonenumber);
        }
        //精确状态查询
        String status = user.getStatus();
        if (StringUtils.isNoneBlank(status)) {
            userLambdaQueryWrapper.eq(User::getStatus, status);
        }
        //分页查询
        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage, userLambdaQueryWrapper);

        //以PageVO封装返回
        PageVO pageVO = new PageVO();
        pageVO.setRows(userPage.getRecords());
        pageVO.setTotal(userPage.getTotal());
        return pageVO;
    }

    @Override
    public GetUserRoleByIdVO getUserRoleById(Long userId) {
        //根据userId获取用户信息
        User user = this.getById(userId);

        //根据userId获取角色id列表
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoleList = userRoleService.list(userRoleLambdaQueryWrapper);
        List<Long> roleIds = userRoleList.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        //获取所有角色信息列表
        List<Role> roles = roleService.list();

        //以VO返回
        GetUserRoleByIdVO userRoleByIdVO = new GetUserRoleByIdVO();
        userRoleByIdVO.setRoleIds(roleIds);
        userRoleByIdVO.setRoles(roles);
        userRoleByIdVO.setUser(user);
        return userRoleByIdVO;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        //修改用户
        boolean b = this.updateById(user);
        if (!b){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Long[] roleIds = user.getRoleIds();
        //修改用户所关联的用户角色
        //删除该userId下的userId
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, user.getId());
        boolean remove = userRoleService.remove(userRoleLambdaQueryWrapper);
        if (!remove){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        //新增userRole
        List<UserRole> userRoleList = Arrays.stream(roleIds)
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        boolean b1 = userRoleService.saveBatch(userRoleList);
        if (!b1){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
}




