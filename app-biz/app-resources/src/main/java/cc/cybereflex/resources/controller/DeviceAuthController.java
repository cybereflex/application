package cc.cybereflex.resources.controller;

import cc.cybereflex.common.constants.JwtConstants;
import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.resources.model.request.DeviceLoginRequest;
import cc.cybereflex.resources.model.response.DeviceLoginResponse;
import cc.cybereflex.resources.service.DeviceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("resources/auth/device")
@RestController
@RequiredArgsConstructor
public class DeviceAuthController {

    private final DeviceServiceImpl deviceService;


    @PostMapping("login")
    public Result<DeviceLoginResponse> login(@RequestBody @Validated DeviceLoginRequest request) {
        Optional<String> token = deviceService.login(request);
        if (token.isPresent()) {
            return Result.success(
                    ResultEnum.SUCCESS,
                    DeviceLoginResponse.builder()
                            .token(token.get())
                            .tokenType(JwtConstants.JWT_TOKEN_TYPE)
                            .build()
            );
        }

        return Result.failed(ResultEnum.AUTH_FAILED);
    }


}
