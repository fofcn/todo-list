package com.epam.common.core;

public enum ResponseCode {

    SUCCESS("00000", "ok"),
    FAILURE("00001", "failure"),
    UNKNOWN_ERROR("00002", "unknown error"),
    HTTP_METHOD_NOT_SUPPORT("00003", "request method not support"),

    BAD_SQL_GRAMMAR("00004", "bad sql grammar"),

    DATA_INTEGRITY("00005", "data integrity violation"),

    HTTP_NOT_FOUND("00006", "service unavailable"),

    PARAMETER_ERROR("00007", "parameter mismatch"),


    USER_NOT_EXIST("A0201", "用户不存在"),

    USERNAME_OR_PASSWORD_ERROR("A0210", "用户名或密码错误"),
    CLIENT_AUTHENTICATION_FAILED("A0212", "客户端认证失败"),
    TOKEN_INVALID_OR_EXPIRED("A0230", "token无效或已过期"),

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
