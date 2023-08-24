package cc.cybereflex.resources.model.request;

import cc.cybereflex.common.model.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonLoginRequest extends BaseRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
