package cc.cybereflex.resources.controller;

import cc.cybereflex.common.constants.JwtConstants;
import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.resources.model.request.PersonLoginRequest;
import cc.cybereflex.resources.model.response.PersonLoginResponse;
import cc.cybereflex.resources.service.PersonServiceImpl;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("resources/auth/person")
@RequiredArgsConstructor
@RestController
public class PersonAuthController {

    private final PersonServiceImpl personService;


    @PostMapping("login")
    public Result<PersonLoginResponse> login(@RequestBody @Validated PersonLoginRequest request) {
        Optional<String> login = personService.login(request);
        if (login.isPresent()) {

            return Result.success(
                    ResultEnum.SUCCESS,
                    PersonLoginResponse.builder()
                            .token(login.get())
                            .tokenType(JwtConstants.JWT_TOKEN_TYPE)
                            .build()
            );
        } else {
            return Result.failed(ResultEnum.AUTH_FAILED);
        }
    }

    @GetMapping("is_unused_username")
    public Result<Void> isUnusedUsername(@NotBlank String username) {
        boolean result = personService.isUnusedUsername(username);
        return result ? Result.success(ResultEnum.SUCCESS) : Result.failed(ResultEnum.PERSON_USERNAME_IS_USED);
    }

}
