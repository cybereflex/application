package cc.cybereflex.resources.controller;

import cc.cybereflex.client.domain.device.DeviceDTO;
import cc.cybereflex.client.domain.enums.DeviceTypeEnum;
import cc.cybereflex.client.domain.enums.VendorEnum;
import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.resources.model.request.DeviceRequest;
import cc.cybereflex.resources.service.DeviceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("resources/manage/device")
@RequiredArgsConstructor
public class DeviceManageController {

    private final DeviceServiceImpl deviceService;

    @PostMapping("add_device")
    public Result<Void> addDevice(@RequestBody @Validated DeviceRequest request){
        DeviceDTO deviceDTO = new DeviceDTO();
        BeanUtils.copyProperties(request, deviceDTO);
        deviceDTO.setType(DeviceTypeEnum.of(request.getType()));
        deviceDTO.setVendor(VendorEnum.of(request.getVendor()));
        boolean result = deviceService.insert(deviceDTO);
        return result ? Result.success(ResultEnum.SUCCESS) : Result.failed(ResultEnum.DEVICE_CREATE_FAILED);
    }



}
