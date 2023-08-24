package cc.cybereflex.resources.model.request;

import cc.cybereflex.common.model.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DeviceLoginRequest extends BaseRequest {

    @NotBlank
    private String ip;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
