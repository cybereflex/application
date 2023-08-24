package cc.cybereflex.dispatch.manager;

import cc.cybereflex.dispatch.enums.RabbitBindingEnum;
import cc.cybereflex.dispatch.model.TerminalWsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Service
@RequestMapping("rabbit")
@RequiredArgsConstructor
public class TerminalDeviceManager {

    private final RabbitTemplate rabbitTemplate;

    @GetMapping("send")
    public void sendMessage(String uuid, String message) throws JsonProcessingException {
        TerminalWsMessage data = TerminalWsMessage.builder()
                .uuid(Collections.singletonList(uuid))
                .message(message)
                .build();
        rabbitTemplate.convertAndSend(
                RabbitBindingEnum.TERMINAL_WEBSOCKET.getExchange().getName(),
                RabbitBindingEnum.TERMINAL_WEBSOCKET.getRoutingKey(),
                new ObjectMapper().writeValueAsString(data)
        );
    }
}
