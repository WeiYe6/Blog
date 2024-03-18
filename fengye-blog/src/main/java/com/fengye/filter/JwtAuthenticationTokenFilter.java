package com.fengye.filter;

import com.alibaba.fastjson.JSON;
import com.fengye.domain.ResponseResult;
import com.fengye.domain.pojo.LoginUser;
import com.fengye.enums.AppHttpCodeEnum;
import com.fengye.utils.JwtUtil;
import com.fengye.utils.RedisCache;
import com.fengye.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * jwt 登录校验过滤器
 *
 * @author fengye
 */
@Component
//博客前台的登录校验过滤器 OncePerRequestFilter是SpringSecurity提供的类
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache; //redis工具类

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token值
        String token = request.getHeader("token");
        //判断是否存在token（若不存在说明该请求不需要登录 直接放行）
        //StringUtils.hasText()用于检查某个字符串是否不为空（null）且至少包含一个非空白的字符，则 hasText 方法返回true。
        //!StringUtils.hasText(token)//加非时 则空返回true 进入
        if (StringUtils.isBlank(token)) {
            //token为空，直接放行
            filterChain.doFilter(request, response);
            return;
        }

        //JwtUtil是 在公共工程写的，解析token，把原来的密文解析为明文
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //解析token 失败时进入
            //当token过期 或者 token被篡改时，会解析失败
            e.printStackTrace();
            //把该异常响应给前端，需要重新登录 ResponseResult、AppHttpCodeEnum、WebUtils是我们在fengye-framework工程写的类
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //从token中获取 保存进token中的数据（userId）
        String userId = claims.getSubject();
        //在redis中，通过key来获取保存进去的登录用户
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        //判断 如果获取数据为空，说明登录过期，需要重新登录
        if (loginUser == null) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //把从redis获取到的value，存入到SecurityContextHolder(Security官方提供的类)
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
