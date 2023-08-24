package cc.cybereflex.client.service;

import cc.cybereflex.client.domain.device.DeviceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "app-resources",path = "client/device")
public interface DeviceService {

    @GetMapping("queryAllAvailable")
    Optional<List<DeviceDTO>> queryAllAvailable();
}
