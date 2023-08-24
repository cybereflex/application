package cc.cybereflex.infrastructure.model.autoconfigure;

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
public class RabbitConfig extends BaseConfig {


    /**
     * rabbitmq 主机名
     */
    private String hostname;
    /**
     * rabbitmq 端口号
     */
    private Integer port;
    /**
     * rabbitmq 用户名
     */
    private String username;
    /**
     * rabbitmq 密码
     */
    private String password;
    /**
     * rabbitmq virtual host
     */
    private String virtualHost;
}
