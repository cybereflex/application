package cc.cybereflex.resources.model.request;

import cc.cybereflex.common.model.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PersonRequest extends BaseRequest {

    @NotBlank(message = "人员姓名不可为空")
    private String name;
    @NotBlank(message = "人员身份证号码不可为空")
    private String idCard;
    @Positive
    private Integer age;
    @NotBlank(message = "人员登陆用户名不可为空")
    private String username;
    @NotBlank(message = "人员密码不可为空")
    private String password;
}
