package cc.cybereflex.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS(100, "请求成功"),
    UNKNOWN_FAILED(999,"未知错误"),
    AUTH_FAILED(1001,"身份认证失败"),
    ARGUMENT_VALID_FAILED(1002, "参数校验失败"),
    PERSON_USERNAME_IS_USED(2001, "人员用户名已存在"),
    PERSON_CREATE_FAILED(2002, "添加人员失败"),
    DEVICE_IP_IS_USED(3001, "设备IP已存在"),
    DEVICE_CREATE_FAILED(3002,"添加设备失败"),


    ;
    private final Integer code;
    private final String message;
}
