package com.fengye.service.impl;

import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.LoginUser;
import com.fengye.domain.pojo.User;
import com.fengye.domain.vo.BlogUserLoginVO;
import com.fengye.domain.vo.UserInfoVO;
import com.fengye.service.BlogLoginService;
import com.fengye.utils.BeanCopyUtils;
import com.fengye.utils.JwtUtil;
import com.fengye.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户登录认证接口（使用 SpringSecurity框架）
 * @author fengye
 */
@Service
//认证，判断用户登录是否成功
public class BlogLoginServiceImpl implements BlogLoginService {

    @Resource
    //AuthenticationManager是security官方提供的接口
    private AuthenticationManager authenticationManager;

    @Resource
    //RedisCache是我们在fengye-framework工程的 util录写redis工具类
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //封装登录的用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //在下一行之前，封装的数据会先走UserDetailsServiceImpl实现类，这个实现类在我们的fengye-framework工程的 service/impl目录里面
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //上面那一行会得到所有的认证用户信息authenticate。然后下一行需要判断用户认证是否通过，如果authenticate的值是null，就说明认证没有通过
        if(authenticate == null){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userId
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //（生成token） 把这个userId通过我们写的JwtUtil工具类转成密文，这个密文就是token值
        String jwt = JwtUtil.createJWT(userId);

        //把登录用户存入 redis缓存中
        //redis的键 K："bloglogin:"+userId   v：登录的用户
        //下面那行的第二个参数: 要把哪个对象存入Redis。我们写的是loginUser，里面有权限信息，后面会用到
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);

        //把User转化为UserInfoVo，封装返回给前端需要的字段信息
        UserInfoVO userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);

        //封装token 和 user信息一起返回给前端
        BlogUserLoginVO vo = new BlogUserLoginVO();
        vo.setToken(jwt);
        vo.setUserInfo(userInfoVo);
        //封装响应返回
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //退出登录：删除redis中的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        Long userId = loginUser.getUser().getId();
        //清空redis
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}