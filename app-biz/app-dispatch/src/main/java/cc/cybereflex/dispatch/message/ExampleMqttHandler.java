package cc.cybereflex.dispatch.message;

import cc.cybereflex.infrastructure.component.AbstractMqttHandler;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class ExampleMqttHandler extends AbstractMqttHandler {
    @Override
    public String getTopic() {
        return "topic1";
    }

    @Override
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {
        System.out.println(message);
    }
}
