package cc.cybereflex.dispatch.websocket;

import cc.cybereflex.common.constants.CacheKeyConstants;
import cc.cybereflex.common.utils.JwtUtil;
import cc.cybereflex.dispatch.enums.DeviceStatusEnum;
import cc.cybereflex.infrastructure.component.RedisCache;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TerminalHandshakeInterceptor implements HandshakeInterceptor {

    private final RedisCache redisCache;

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {

        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeaders) || authHeaders.size() > 1) {
            return true;
        }
        String token = authHeaders.get(0);

        String uuid = JwtUtil.parseUID(token);

        redisCache.setMapValue(CacheKeyConstants.CACHE_KEY_ONLINE_TERMINAL,uuid, DeviceStatusEnum.ONLINE.name());

        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, Exception exception) {

    }
}
