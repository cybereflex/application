package cc.cybereflex.dispatch.manager;

import cc.cybereflex.dispatch.enums.MqttTopicEnum;
import cc.cybereflex.infrastructure.component.MqttTemplate;
import cc.cybereflex.infrastructure.model.mqtt.MqttMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("mqtt")
public class MqttProducer {

    private final MqttTemplate mqttTemplate;

    @GetMapping("send")
    public void send() throws Exception {
        MqttMessage message = MqttMessage.builder()
                .uuid(UUID.randomUUID().toString())
                .build();
        boolean result = mqttTemplate.convertAndSend(message, MqttTopicEnum.TOPIC_1.getTopic(), MqttTopicEnum.TOPIC_1.getQos(), 5000L);
        System.out.println(result);
    }


}
