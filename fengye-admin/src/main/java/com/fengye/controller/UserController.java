package com.fengye.controller;


import com.fengye.domain.ResponseResult;
import com.fengye.domain.dto.UpdateUserStatusDto;
import com.fengye.domain.pojo.User;
import com.fengye.domain.vo.GetUserRoleByIdVO;
import com.fengye.domain.vo.PageVO;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.exception.SystemException;
import com.fengye.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fengye
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    private UserService userService;

    //分页查询用户
    @GetMapping("/list")
    public ResponseResult getUserPageList(Integer pageNum, Integer pageSize, User user) {
        PageVO pageVO = userService.getUserPageList(pageNum, pageSize, user);
        return ResponseResult.okResult(pageVO);
    }

    //新增用户
    @PostMapping("")
    public ResponseResult addUser(@RequestBody User user) {
        if (user == null) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        if (StringUtils.isAnyBlank(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USER_NAME_NULL_ERROR);
        }
        if (StringUtils.isAnyBlank(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.USER_NICKNAME_NULL_ERROR);
        }
        if (StringUtils.isAnyBlank(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.USER_PASSWORD_NULL_ERROR);
        }
        return userService.register(user);
    }

    //修改用户
    //根据id查询用户
    @GetMapping("{userId}")
    public ResponseResult getUserRoleById(@PathVariable("userId") Long userId) {
        if (userId == null || userId <= 0) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        GetUserRoleByIdVO getUserRoleByIdVO = userService.getUserRoleById(userId);
        return ResponseResult.okResult(getUserRoleByIdVO);
    }

    @PutMapping("")
    public ResponseResult updateUser(@RequestBody User user) {
        if (user == null) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

    //修改用户状态
    @PutMapping("/changeStatus")
    public ResponseResult updateUserStatus(@RequestBody UpdateUserStatusDto updateUserStatusDto) {
        if (updateUserStatusDto == null) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        User user = new User();
        user.setId(Long.valueOf(updateUserStatusDto.getUserId()));
        user.setStatus(updateUserStatusDto.getStatus());
        boolean b = userService.updateById(user);
        if (!b) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    //删除用户
//    @DeleteMapping("/{id}")
//    public ResponseResult deleteUserById(@PathVariable("id") Long id) {
//        if (id == null || id < 0) {
//            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
//        }
//        boolean b = userService.removeById(id);
//        if (!b) {
//            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
//        }
//        return ResponseResult.okResult();
//    }

    //批量删除用户兼容单删除用户了
    @DeleteMapping("/{ids}")
    public ResponseResult deleteAllUser(@PathVariable("ids") String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new SystemException(AppHttpCodeEnum.PARAM_EXIST);
        }
        String[] strings = ids.split(",");
        for (String id : strings) {
            userService.removeById(id);
        }
        return ResponseResult.okResult();
    }

}