package com.fengye.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回给前端用户信息的 VO封装类
 * @author fengye
 */
@Data
@Accessors(chain = true)
public class UserInfoVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;

}