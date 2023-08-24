package cc.cybereflex.resources.model.response;

import cc.cybereflex.common.model.BaseResponse;
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
public class DeviceLoginResponse extends BaseResponse {

    private String token;
    private String tokenType;

}
