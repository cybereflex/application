package cc.cybereflex.client.domain.device;

import cc.cybereflex.client.domain.enums.DeviceTypeEnum;
import cc.cybereflex.client.domain.enums.VendorEnum;
import cc.cybereflex.common.model.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DeviceDTO extends BaseDTO {

    private Long id;
    private String uuid;
    private String name;
    private String ip;
    private String port;
    private String username;
    private String password;
    private VendorEnum vendor;
    private DeviceTypeEnum type;
    private Boolean enable;
    private Long version;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;
    private Map<String,Object> extraInfo;
}
