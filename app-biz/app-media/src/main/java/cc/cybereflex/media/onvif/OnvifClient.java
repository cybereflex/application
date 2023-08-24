package cc.cybereflex.media.onvif;

import cc.cybereflex.common.model.Result;
import cc.cybereflex.media.onvif.command.*;
import cc.cybereflex.media.onvif.common.DiscoveryCallback;
import cc.cybereflex.media.onvif.model.OnvifDevice;
import cc.cybereflex.media.onvif.model.OnvifDeviceCapability;
import cc.cybereflex.media.onvif.model.OnvifDeviceInfo;
import cc.cybereflex.media.onvif.model.OnvifDeviceProfile;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class OnvifClient {

    private final String ip;
    private final Integer port;
    private final String username;
    private final String password;
    private final OnvifDevice onvifDevice;

    public OnvifClient(String ip, Integer port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.onvifDevice = init();
    }

    public OnvifClient(OnvifDevice onvifDevice) {
        this.ip = onvifDevice.getIp();
        this.port = onvifDevice.getPort();
        this.username = onvifDevice.getUsername();
        this.password = onvifDevice.getPassword();
        this.onvifDevice = onvifDevice;
    }


    /**
     * Onvif device discovery
     *
     * @param timeout     设备发现总等待时长
     * @param timeoutUnit 总等待时长单位
     * @param waitTime    单个线程等待组播播结果时长，单位毫秒
     * @param callback    回调函数
     */
    public static void discovery(long timeout, TimeUnit timeoutUnit, int waitTime, DiscoveryCallback callback) {
        new DeviceDiscoveryCommand(callback, timeout, timeoutUnit, waitTime).execute();
    }

    /**
     * onvif 设备重启
     */
    public void reboot() {
        new SystemRebootCommand(onvifDevice).execute();
    }


    /**
     * 获取 onvif 设备的 rtsp url
     */
    public Optional<List<String>> getRtspUrl() {
        if (CollectionUtils.isEmpty(onvifDevice.getProfiles())) {
            return Optional.empty();
        }

        return Optional.of(
                onvifDevice.getProfiles().parallelStream()
                        .map(it -> new GetRtspUriCommand(onvifDevice, it.getToken()).execute())
                        .filter(Result::isSuccess)
                        .map(Result::getData)
                        .toList()
        );
    }

    /**
     * @param x    x轴移动
     * @param y    y轴移动
     * @param zoom 变焦
     */
    public void ptz(double x, double y, double zoom) {
        Assert.isTrue(x >= -1 && x <= 1, "x 必须大于等于-1以及小于等于1");
        Assert.isTrue(y >= -1 && y <= 1, "y 必须大于等于-1以及小于等于1");
        Assert.isTrue(zoom >= -1 && zoom <= 1, "zoom 必须大于等于-1以及小于等于1");

        if (!this.onvifDevice.getCapabilities().contains(OnvifDeviceCapability.PTZ)) {
            return;
        }
        List<OnvifDeviceProfile> profiles = onvifDevice.getProfiles();
        if (CollectionUtils.isEmpty(profiles)) {
            return;
        }

        new DevicePtzCommand(onvifDevice, profiles.get(0).getToken(), x, y, zoom).execute();
    }


    private OnvifDevice init() {
        //get capability
        Result<OnvifDevice> capabilityResult = new GetCapabilitiesCommand(ip, port, username, password).execute();
        if (!capabilityResult.isSuccess()) {
            throw new RuntimeException("get capabilities failed");
        }
        OnvifDevice onvifDevice = capabilityResult.getData();
        //must have device capability
        if (!onvifDevice.getCapabilities().contains(OnvifDeviceCapability.DEVICE)) {
            throw new RuntimeException("unsupported onvif device");
        }
        //get device info
        Result<OnvifDeviceInfo> deviceInfoResult = new GetDeviceInfoCommand(onvifDevice).execute();
        if (deviceInfoResult.isSuccess()) {
            onvifDevice.setOnvifDeviceInfo(deviceInfoResult.getData());
        }

        //get media profiles
        if (onvifDevice.getCapabilities().contains(OnvifDeviceCapability.MEDIA)) {
            Result<List<OnvifDeviceProfile>> profiles = new GetProfilesCommand(onvifDevice).execute();
            if (profiles.isSuccess()) {
                onvifDevice.setProfiles(profiles.getData());
            }
        }

        return onvifDevice;
    }

}
