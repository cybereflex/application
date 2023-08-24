package cc.cybereflex.dispatch.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MqttTopicEnum {
    TOPIC_1("topic1",2)
    ;
    private final String topic;
    private final int qos;
}
