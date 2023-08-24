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
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PersonLoginResponse extends BaseResponse {

    private String token;
    private String tokenType;
}
