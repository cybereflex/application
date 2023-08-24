package cc.cybereflex.infrastructure.autoconfigure.message;

import cc.cybereflex.infrastructure.autoconfigure.AbstractAutoConfiguration;
import cc.cybereflex.infrastructure.component.AbstractMqttHandler;
import cc.cybereflex.infrastructure.component.MqttTemplate;
import cc.cybereflex.infrastructure.model.autoconfigure.MqttConfig;
import cc.cybereflex.infrastructure.model.constants.AutoconfigureConstants;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public class MqttAutoConfiguration extends AbstractAutoConfiguration<MqttConfig> {


    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        super.postProcessBeanDefinitionRegistry(registry);

        prepareConfig().ifPresent(config -> {

            Optional<List<AbstractMqttHandler>> beans = getBeans(AbstractMqttHandler.class);

            beans.ifPresent(handlers -> {

                //registry inbound channel
                for (int i = 0; i < handlers.size(); i++) {
                    AbstractMqttHandler handler = handlers.get(i);
                    StandardIntegrationFlow mqttInboundFlow = IntegrationFlow.from(
                                    new MqttPahoMessageDrivenChannelAdapter(
                                            config.getUri(),
                                            config.getClientId(),
                                            handler.getTopic()
                                    )
                            )
                            .handle(handler)
                            .get();

                    registerBean(String.format(AutoconfigureConstants.MQTT_INBOUND_FLOW_BEAN_NAME, i), IntegrationFlow.class, mqttInboundFlow);
                }

                //registry outbound channel
                IntegrationFlow mqttOutboundFlow = flow ->
                        flow.handle(new MqttPahoMessageHandler(
                                config.getUri(),
                                config.getClientId()
                        ));
                registerBean(AutoconfigureConstants.MQTT_OUTBOUND_FLOW_BEAN_NAME, IntegrationFlow.class, mqttOutboundFlow);

                //registry mqtt template
                registerBean(AutoconfigureConstants.MQTT_TEMPLATE_BEAN_NAME, MqttTemplate.class, new MqttTemplate(mqttOutboundFlow));

            });

        });


    }

    @Override
    protected Optional<MqttConfig> prepareConfig() {
        Boolean enable = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_MQTT + "enable", Boolean.class);
        String uri = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_MQTT + "uri", String.class);
        String clientId = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_MQTT + "client-id", String.class);
        if (BooleanUtils.isNotTrue(enable)
                || StringUtils.isBlank(uri)
                || StringUtils.isBlank(clientId)) {
            return Optional.empty();
        }

        String username = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_MQTT + "username", String.class);
        String password = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_MQTT + "password", String.class);

        return Optional.of(
                MqttConfig.builder()
                        .enable(enable)
                        .uri(uri)
                        .clientId(clientId)
                        .username(username)
                        .password(password)
                        .build()
        );
    }
}
