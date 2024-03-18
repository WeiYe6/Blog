package com.fengye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengye.constants.SystemConstants;
import com.fengye.domain.pojo.LoginUser;
import com.fengye.domain.pojo.User;
import com.fengye.mapper.MenuMapper;
import com.fengye.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 重写SpringSecurity的校验配置类
 * @author fengye
 */
//当fengye-blog工程的 BlogLoginServiceImpl类封装好登录的用户名和密码之后，就会传到当前这个实现类
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    //UserMapper是我们在fengye-framework工程mapper目录的接口
    private UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    //在这里之前，我们已经拿到了登录的用户名和密码。UserDetails是SpringSecurity官方提供的接口
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名(唯一) 去查询用户信息(数据库是否有这个用户名)
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);

        //判断是否查询到用户，也就是这个用户是否存在，如果没查到就抛出异常
        if(user == null){
            throw new RuntimeException("用户不存在"); //后期会对异常进行统一处理
        }

        //查询权限信息，并封装
        //只对后台用户进行权限校验(管理员: 后台用户才有权限进入后台系统对数据进行管理)
        if (user.getType().equals(SystemConstants.IS_ADMIN)){
            //根据用户id，查询权限关键词
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, list);
        }

        //如果不是后台用户，不需要封装权限信息
        //返回查询到的用户信息。注意下面那行直接返回user会报错，我们需要在fengye-framework工程的domain目录新
        //建LoginUser类，在LoginUser类实现UserDetails接口，然后下面那行就返回LoginUser对象
        return new LoginUser(user, null);
    }
}