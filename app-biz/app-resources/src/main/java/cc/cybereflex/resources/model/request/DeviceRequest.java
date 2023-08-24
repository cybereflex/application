package cc.cybereflex.resources.model.request;

import cc.cybereflex.common.model.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DeviceRequest extends BaseRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String ip;
    @NotBlank
    private String port;
    private String username;
    private String password;
    @NotBlank
    private String vendor;
    @NotBlank
    private Integer type;
    private Map<String,Object> extraInfo;
}
