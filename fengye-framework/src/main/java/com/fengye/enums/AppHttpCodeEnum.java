package com.fengye.enums;

/**
 * 统一响应格式 （自定义错误码枚举类）
 * @author fengye
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    PARAM_EXIST(506,"参数传递错误"),
    CONTENT_NOT_NULL(507, "发送的评论内容不能为空"),
    FILE_TYPE_ERROR(508, "文件类型错误，请上传jpg/png文件"),
    USER_NAME_NULL_ERROR(509, "用户名不能为空"),
    USER_NICKNAME_NULL_ERROR(601, "昵称不能为空"),
    USER_PASSWORD_NULL_ERROR(602, "密码不能为空"),
    DATA_NULL_ERROR(603, "数据为空"),
    FILE_SIZE_ERROR(413, "文件大小不能超出2MB");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}