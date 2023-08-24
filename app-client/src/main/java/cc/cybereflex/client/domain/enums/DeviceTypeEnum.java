package cc.cybereflex.client.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum DeviceTypeEnum {
    ALARM_DEVICE(10, "报警设备"),
    WEAR_DEVICE(20, "穿戴设备"),
    MONITOR_DEVICE(30, "监控设备"),

    ;
    private final Integer code;
    private final String name;

    public static DeviceTypeEnum of(Integer code) {
        for (DeviceTypeEnum e : values()) {
            if (Objects.equals(code,e.getCode())){
                return e;
            }
        }

        throw new IllegalArgumentException("illegal device type code");
    }
}
