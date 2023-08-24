package cc.cybereflex.dispatch.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class TerminalWebSocketHandler implements WebSocketHandler {

    private final TerminalSessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        Assert.notNull(uri, "the websocket uri can not be null");
        String uuid = parseUUID(uri);
        sessionManager.register(uuid, session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {


    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        URI uri = session.getUri();
        Assert.notNull(uri, "the websocket uri can not be null");
        String uuid = parseUUID(uri);
        sessionManager.remove(uuid);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    private String parseUUID(URI uri) {
        String queryString = uri.getQuery();

        Optional<String> uuid = Arrays.stream(queryString.split("&"))
                .filter(it -> it.startsWith("uuid"))
                .findFirst();

        Assert.isTrue(uuid.isPresent(), "can not found device uuid");
        String[] split = uuid.get().split("=");
        Assert.isTrue(split.length == 2, "can not found device uuid");
        return split[1];
    }

}
