package com.epam.common.core;

public enum ResponseCode {

    SUCCESS("00000", "ok"),
    FAILURE("00001", "failure"),
    UNKNOWN_ERROR("00002", "unknown error"),
    HTTP_METHOD_NOT_SUPPORT("00003", "request method not support"),

    BAD_SQL_GRAMMAR("00004", "bad sql grammar"),

    DATA_INTEGRITY("00005", "data integrity violation"),

    HTTP_NOT_FOUND("00006", "data integrity violation"),

    PARAMETER_ERROR("00007", ""),

    USER_ERROR("A0001", "用户端错误"),
    USER_LOGIN_ERROR("A0200", "用户登录异常"),

    USER_NOT_EXIST("A0201", "用户不存在"),
    USER_ACCOUNT_LOCKED("A0202", "用户账户被冻结"),
    USER_ACCOUNT_INVALID("A0203", "用户账户已作废"),

    USERNAME_OR_PASSWORD_ERROR("A0210", "用户名或密码错误"),
    PASSWORD_ENTER_EXCEED_LIMIT("A0211", "用户输入密码次数超限"),
    CLIENT_AUTHENTICATION_FAILED("A0212", "客户端认证失败"),
    TOKEN_INVALID_OR_EXPIRED("A0230", "token无效或已过期"),
    TOKEN_ACCESS_FORBIDDEN("A0231", "token已被禁止访问"),

    TOKEN_REFRESH_INVALID_OR_EXPIRED("A0232", "refresh token无效或已过期"),

    AUTHORIZED_ERROR("A0300", "访问权限异常"),
    ACCESS_UNAUTHORIZED("A0301", "访问未授权"),

    TASK_OWNER_ERROR("A1001", "请删除自己任务"),
    TASK_NOT_EXISTING("A1002", "该任务不存在"),
    TASK_STATE_TRANSITION_ERROR("A1003", "该任务不存在"),

    USER_EXISTS("A2001", "User existing"),
    ;

    private String code;

    private String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
