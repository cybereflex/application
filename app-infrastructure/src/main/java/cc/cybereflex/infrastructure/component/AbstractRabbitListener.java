package cc.cybereflex.infrastructure.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractRabbitListener<T> implements ChannelAwareMessageListener {

    public abstract String getRoutingKey();

    public abstract Set<Queue> getQueues();

    public abstract Exchange getExchange();

    public abstract Map<String, Object> getArguments();

    public abstract AcknowledgeMode getAcknowledgeMode();

    public abstract void onMessage(T message);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        if (!Objects.equals(AcknowledgeMode.MANUAL, getAcknowledgeMode())){
            T value = new ObjectMapper()
                    .readValue(new String(message.getBody()), new TypeReference<>() {});

            onMessage(value);
        }
    }
}
