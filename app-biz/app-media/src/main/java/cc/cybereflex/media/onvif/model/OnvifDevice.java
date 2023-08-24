package cc.cybereflex.media.onvif.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnvifDevice {
    /**
     * ip 地址
     */
    private String ip;
    /**
     * onvif 协议端口号
     */
    private int port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * ptz capability url
     */
    private String ptzUrl;
    /**
     * media capability url
     */
    private String mediaUrl;
    /**
     * device service capability url
     */
    private String deviceUrl;
    /**
     * analytics capability url
     */
    private String analyticsUrl;
    /**
     * events capability url
     */
    private String eventsUrl;
    /**
     * imaging capability url
     */
    private String imagingUrl;

    /**
     * onvif 设备拥有的能力
     */
    private List<OnvifDeviceCapability> capabilities;

    /**
     * 设备信息
     */
    private OnvifDeviceInfo onvifDeviceInfo;


    /**
     * media profiles
     */
    private List<OnvifDeviceProfile> profiles;


    public void addCapability(OnvifDeviceCapability capability) {
        if (CollectionUtils.isEmpty(this.capabilities)) {
            this.capabilities = new ArrayList<>();
        }

        this.capabilities.add(capability);
    }


}
