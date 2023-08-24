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
public class RedisConfig extends BaseConfig{

    /**
     * redis 用户名
     */
    private String username;
    /**
     * redis 密码
     */
    private String password;
    /**
     * redis 主机名
     */
    private String hostname;
    /**
     * redis 端口
     */
    private Integer port;
    /**
     * 要链接的 redis 的 database
     */
    private Integer database;
}
