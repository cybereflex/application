package cc.cybereflex.infrastructure.model.mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqttMessage {
    /**
     * 消息的唯一id
     */
    private String uuid;


}
