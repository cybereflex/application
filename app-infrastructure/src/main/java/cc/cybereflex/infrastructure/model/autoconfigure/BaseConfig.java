package cc.cybereflex.infrastructure.model.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseConfig {

    /**
     * 是否启用
     */
    private Boolean enable;
}
