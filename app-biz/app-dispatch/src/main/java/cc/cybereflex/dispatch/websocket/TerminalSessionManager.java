package cc.cybereflex.dispatch.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TerminalSessionManager {

    private static final Logger logger = LoggerFactory.getLogger(TerminalSessionManager.class);

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();


    public void register(@NonNull String key, @NonNull WebSocketSession session) {
        if (!sessionMap.containsKey(key)) {
            sessionMap.put(key, session);
        }
    }

    public void remove(@NonNull String key){
        sessionMap.remove(key);
    }

    public void broadcast(String message, String... deviceUUID) {
        List<String> uuidList = List.of(deviceUUID);
        sessionMap.entrySet()
                .stream()
                .filter(entry -> uuidList.contains(entry.getKey()))
                .forEach(
                        entry -> {
                            try {
                                entry.getValue().sendMessage(new TextMessage(message));
                            } catch (IOException e) {
                                logger.error("send message to {} error", entry.getKey());
                            }

                        }
                );
    }


}
