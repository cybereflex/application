package cc.cybereflex.infrastructure.model.autoconfigure;

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
public class MqttConfig extends BaseConfig {

    private String uri;
    private String username;
    private String password;
    private String clientId;
}
