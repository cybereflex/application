package cc.cybereflex.dispatch.websocket;

import cc.cybereflex.infrastructure.component.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final TerminalSessionManager sessionManager;
    private final RedisCache redisCache;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {

        registry.addHandler(new TerminalWebSocketHandler(sessionManager), "ws/terminal")
                .addInterceptors(
                        new TerminalHandshakeInterceptor(redisCache)
                )
                .setAllowedOrigins("*");
    }
}
