package com.fengye.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录 返回给前端统一格式的VO封装类
 * @author fengye
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserLoginVO {

    private String token;
    private UserInfoVO userInfo;
}