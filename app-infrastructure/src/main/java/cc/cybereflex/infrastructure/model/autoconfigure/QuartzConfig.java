package cc.cybereflex.infrastructure.model.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QuartzConfig extends BaseConfig{

    private String jobStoreType;
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
}
