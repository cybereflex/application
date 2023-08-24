package cc.cybereflex.media.onvif.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnvifDeviceInfo {
    /**
     * 生产厂商
     */
    private String manufacturer;
    /**
     * 型号
     */
    private String model;
    /**
     * 固件版本
     */
    private String firmwareVersion;
    /**
     * 序列号
     */
    private String serialNumber;
    /**
     * 硬件ID
     */
    private String hardwareId;
}
