package cc.cybereflex.dispatch.message;

import cc.cybereflex.dispatch.enums.RabbitBindingEnum;
import cc.cybereflex.dispatch.model.TerminalWsMessage;
import cc.cybereflex.dispatch.websocket.TerminalSessionManager;
import cc.cybereflex.infrastructure.component.AbstractRabbitListener;
import jakarta.annotation.Resource;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class TerminalWebsocketListener extends AbstractRabbitListener<TerminalWsMessage> {

    @Resource
    private TerminalSessionManager terminalSessionManager;

    @Override
    public String getRoutingKey() {
        return RabbitBindingEnum.TERMINAL_WEBSOCKET.getRoutingKey();
    }

    @Override
    public Set<Queue> getQueues() {
        return RabbitBindingEnum.TERMINAL_WEBSOCKET.getQueues();
    }

    @Override
    public Exchange getExchange() {
        return RabbitBindingEnum.TERMINAL_WEBSOCKET.getExchange();
    }

    @Override
    public Map<String, Object> getArguments() {
        return RabbitBindingEnum.TERMINAL_WEBSOCKET.getArgs();
    }

    @Override
    public AcknowledgeMode getAcknowledgeMode() {
        return RabbitBindingEnum.TERMINAL_WEBSOCKET.getAcknowledgeMode();
    }

    @Override
    public void onMessage(TerminalWsMessage message) {
        terminalSessionManager.broadcast(message.getMessage(), message.getUuid().toArray(String[]::new));
    }
}
