package cc.cybereflex.gateway.router;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class DynamicRouteConfiguration implements ApplicationEventPublisherAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicRouteConfiguration.class);

    private final RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.config.username}")
    private String username;
    @Value("${spring.cloud.nacos.config.password}")
    private String password;
    @Value("${app.gateway.route.dataId}")
    private String dataId;
    @Value("${app.gateway.route.group}")
    private String group;
    @Value("${app.gateway.route.timeoutMills}")
    private Long timeoutMills;

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("namespace", namespace);
            properties.put("username", username);
            properties.put("password", password);
            ConfigService configService = NacosFactory.createConfigService(properties);
            String routeConfig = configService.getConfig(dataId, group, timeoutMills);
            refreshRoute(routeConfig);
            configService.addListener(dataId, namespace, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    refreshRoute(configInfo);
                }
            });

        } catch (Exception e) {
            logger.error("init nacos dynamic route failed", e);
        }
    }


    public void refreshRoute(String config) {
        if (StringUtils.isBlank(config)) return;
        try {
            List<RouteDefinition> routeDefinitions = new ObjectMapper()
                    .readValue(config, new TypeReference<>() {
                    });

            routeDefinitions.forEach(it -> {
                routeDefinitionWriter.save(Mono.just(it)).subscribe();
                applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));
            });


        } catch (Exception e) {
            logger.error("refresh nacos route error", e);
        }
    }


}
