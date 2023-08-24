package cc.cybereflex.gateway.filter;

import cc.cybereflex.common.constants.JwtConstants;
import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.common.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthorizationGlobalFilter implements GlobalFilter, Ordered, EnvironmentAware {
    private Environment environment;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //match white list url
        String whitelistUrl = environment.getProperty("whitelist", String.class);
        if (StringUtils.isNotBlank(whitelistUrl)){
            Set<String> urls = Arrays.stream(whitelistUrl.split(",")).collect(Collectors.toSet());
            String path = request.getPath().value();
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            if (CollectionUtils.isNotEmpty(urls)){
                Optional<String> isMatch = urls.stream().filter(it -> antPathMatcher.match(it, path)).findFirst();
                if (isMatch.isPresent()) {
                    return chain.filter(exchange);
                }
            }
        }

        //authentication
        HttpHeaders headers = request.getHeaders();
        List<String> authHeaders = headers.get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeaders)) {
            return errorResponse(response);
        }

        Optional<String> authHeader = authHeaders.stream()
                .filter(it -> it.startsWith(JwtConstants.JWT_AUTH_HEADER_PREFIX))
                .findFirst();
        if (authHeader.isEmpty()) {
            return errorResponse(response);
        }

        String token = authHeader.get().replace(JwtConstants.JWT_AUTH_HEADER_PREFIX, "");
        if (JwtUtil.isInvalidToken(token)) {
            return errorResponse(response);
        }

        //replace token
        return chain.filter(
                exchange.mutate()
                        .request(
                                request.mutate()
                                        .header(HttpHeaders.AUTHORIZATION, token)
                                        .build()
                        )
                        .build()
        );
    }

    private Mono<Void> errorResponse(ServerHttpResponse response) throws JsonProcessingException {

        response.setStatusCode(HttpStatus.OK);

        HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String ret = new ObjectMapper()
                .writeValueAsString(Result.failed(ResultEnum.AUTH_FAILED));

        Flux<DataBuffer> data = Flux.just(
                response.bufferFactory()
                        .wrap(ret.getBytes(StandardCharsets.UTF_8))
        );

        return response.writeWith(data);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
