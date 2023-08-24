package cc.cybereflex.dispatch.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum RabbitBindingEnum {

    TERMINAL_WEBSOCKET(
            "",
            Set.of(new Queue("terminal-ws-queue", true, false, false)),
            ExchangeBuilder.fanoutExchange("terminal-ws-fan-exchange").durable(true).build(),
            AcknowledgeMode.AUTO,
            Collections.emptyMap()
    ),


    ;
    private final String routingKey;
    private final Set<Queue> queues;
    private final Exchange exchange;
    private final AcknowledgeMode acknowledgeMode;
    private final Map<String, Object> args;
}
