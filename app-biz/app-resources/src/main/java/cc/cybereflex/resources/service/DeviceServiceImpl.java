package cc.cybereflex.resources.service;

import cc.cybereflex.client.domain.device.DeviceDTO;
import cc.cybereflex.client.domain.enums.DeviceTypeEnum;
import cc.cybereflex.client.domain.enums.VendorEnum;
import cc.cybereflex.client.service.DeviceService;
import cc.cybereflex.common.model.UserTypeEnum;
import cc.cybereflex.common.utils.JwtUtil;
import cc.cybereflex.resources.model.data.Device;
import cc.cybereflex.resources.model.request.DeviceLoginRequest;
import cc.cybereflex.resources.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("client/device")
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    @GetMapping("queryAllAvailable")
    public Optional<List<DeviceDTO>> queryAllAvailable() {
        List<Device> devices = deviceRepository.queryAllAvailable();
        if (CollectionUtils.isNotEmpty(devices)) {
            return Optional.of(
                    devices.stream().map(it -> {
                        DeviceDTO deviceDTO = new DeviceDTO();
                        BeanUtils.copyProperties(it, deviceDTO);
                        deviceDTO.setVendor(VendorEnum.of(it.getVendor()));
                        deviceDTO.setType(DeviceTypeEnum.of(it.getType()));
                        return deviceDTO;
                    }).toList()
            );
        }
        return Optional.empty();
    }


    public boolean insert(DeviceDTO deviceDTO){
        try {
            Device device = new Device();
            BeanUtils.copyProperties(deviceDTO,device);

            String password = deviceDTO.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            device.setPassword(passwordEncoder.encode(password));
            device.setType(deviceDTO.getType().getCode());
            device.setVendor(deviceDTO.getVendor().getCode());
            device.setUuid(UUID.randomUUID().toString());
            deviceRepository.insert(device);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Optional<String> login(DeviceLoginRequest request) {
        Device device = deviceRepository.queryByIpAndUsername(request.getIp(), request.getUsername());
        if (Objects.isNull(device)) {
            return Optional.empty();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = device.getPassword();
        String password = request.getPassword();

        if (passwordEncoder.matches(password, encodePassword)) {
            String token = JwtUtil.generateToken(
                    device.getUuid(), device.getUsername(),
                    device.getIp(), UserTypeEnum.DEVICE,
                    Collections.emptyMap()
            );
            return Optional.of(token);
        }

        return Optional.empty();
    }



}
