package com.fengye.config;

import com.fengye.filter.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * SpringSecurity 配置类
 *
 * @author fengye
 */
@Configuration
//WebSecurityConfigurerAdapter是Security官方提供的类
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    //注入我们在fengye-blog工程写的JwtAuthenticationTokenFilter过滤器
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    //注入官方认证失败的处理器。注意不用写private，并且不是注入我们自定义的认证失败处理器。理由:符合开闭原则
    //虽然我们注入的不是自己写的认证失败处理器，但是最终用的实际上就是我们写的，Security会调用我们自己实现的
    AuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    //注入官方授权失败的处理器。注意不用写private，并且不是注入我们自定义的授权失败处理器。理由:符合开闭原则
    //虽然我们注入的不是自己写的授权失败处理器，但是最终用的实际上就是我们写的，Security会调用我们自己实现的
    AccessDeniedHandler accessDeniedHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    //把官方的PasswordEncoder密码加密方式替换成BCryptPasswordEncoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/login").anonymous()

//                //测试用的--为方便测试认证过滤器，我们把查询 友链的接口设置为需要登录才能访问。然后我们去访问的时候就能测试登录认证功能了
//                .antMatchers("/link/getAllLink").authenticated()
                  //查询个人信息，需要登录权限（登录才能访问）
                .antMatchers("/user/userInfo").authenticated()

                //把文件上传的接口 设置为需要登录才能访问
//                .antMatchers("/upload").authenticated()

                //退出登录的配置。如果'没登录'就调用'退出登录'，会报错'401 需要登录后操作'，也就是authenticated
                .antMatchers("/logout").authenticated()

                //需要登录才能在评论区发送评论
                .antMatchers("/comment").authenticated()

                // 除上面外的所有请求全部不需要认证即可访问
                .anyRequest().permitAll();

        //把我们写的自定义异常处理器配置给Security
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        http.logout().disable();

        //把我们在fengye-blog工程写的JwtAuthenticationTokenFilter过滤器添加到Security的过滤器链中
        //第一个参数是你要添加的过滤器，第二个参数是你想把你的过滤器添加到哪个security官方过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //允许跨域
        http.cors();
    }

}