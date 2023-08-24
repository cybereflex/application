package cc.cybereflex.infrastructure.component;

import cc.cybereflex.infrastructure.model.mqtt.MqttMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class MqttTemplate {

    private final IntegrationFlow mqttOutboundFlow;

    public <T extends MqttMessage> boolean convertAndSend(@NonNull T data, @NonNull String topic, @NonNull Integer qos, Long timeout) throws Exception {
        MessageChannel inputChannel = mqttOutboundFlow.getInputChannel();
        Assert.notNull(inputChannel, "mqtt input channel must be not null");

        String json = new ObjectMapper().writeValueAsString(data);

        Message<String> message = MessageBuilder.withPayload(json)
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.QOS, qos)
                .build();

        return inputChannel.send(message, Objects.isNull(timeout) ? 3000L : timeout);
    }


}
