package cc.cybereflex.infrastructure.component;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public abstract class AbstractMqttHandler implements MessageHandler {

    public abstract String getTopic();

    @Override
    public abstract void handleMessage(@NonNull Message<?> message) throws MessagingException;
}
