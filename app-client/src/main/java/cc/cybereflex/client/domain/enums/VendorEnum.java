package cc.cybereflex.client.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Getter
public enum VendorEnum {
    OWNER("owner", "自研设备"),

    ;
    private final String code;
    private final String name;

    public static VendorEnum of(String code){
        for (VendorEnum e : values()) {
            if (StringUtils.endsWith(e.getCode(),code)){
                return e;
            }
        }

        throw new IllegalArgumentException("illegal vendor code");
    }
}
