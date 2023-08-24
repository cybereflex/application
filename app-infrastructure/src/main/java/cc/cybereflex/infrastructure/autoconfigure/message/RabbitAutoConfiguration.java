package cc.cybereflex.infrastructure.autoconfigure.message;

import cc.cybereflex.infrastructure.autoconfigure.AbstractAutoConfiguration;
import cc.cybereflex.infrastructure.component.AbstractRabbitListener;
import cc.cybereflex.infrastructure.model.autoconfigure.RabbitConfig;
import cc.cybereflex.infrastructure.model.constants.AutoconfigureConstants;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.lang.NonNull;

import java.util.*;

public class RabbitAutoConfiguration extends AbstractAutoConfiguration<RabbitConfig> {

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        super.postProcessBeanDefinitionRegistry(registry);

        try {
            prepareConfig().ifPresent(config -> {

                //registry connection factory
                CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
                connectionFactory.setHost(config.getHostname());
                connectionFactory.setPort(config.getPort());
                connectionFactory.setUsername(config.getUsername());
                connectionFactory.setPassword(config.getPassword());
                connectionFactory.setVirtualHost(config.getVirtualHost());
                registerBean(AutoconfigureConstants.RABBIT_CONNECTION_FACTORY_BEAN_NAME, ConnectionFactory.class, connectionFactory);

                //registry rabbit template
                RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
                registerBean(AutoconfigureConstants.RABBIT_TEMPLATE_BEAN_NAME, RabbitTemplate.class, rabbitTemplate);

                //registry rabbit admin
                RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
                registerBean(AutoconfigureConstants.RABBIT_ADMIN_BEAN_NAME, RabbitAdmin.class, rabbitAdmin);

                //init binding
                initBinding(rabbitAdmin, connectionFactory);
            });

        } catch (Exception e) {
            logger.error("rabbitmq autoconfigure failed, {}", e.getMessage());
            throw new BeanCreationException(e.getMessage());
        }

    }


    @SuppressWarnings("unchecked")
    private void initBinding(RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory) {
        getBeans(AbstractRabbitListener.class)
                .ifPresent(listeners ->
                        listeners.forEach(listener -> {
                            String routingKey = listener.getRoutingKey();
                            Exchange exchange = listener.getExchange();
                            Set<Queue> queues = listener.getQueues();
                            Map<String, Object> arguments = listener.getArguments();

                            rabbitAdmin.declareExchange(exchange);
                            queues.forEach(queue -> {
                                rabbitAdmin.declareQueue(queue);
                                Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).and(arguments);
                                rabbitAdmin.declareBinding(binding);
                            });

                            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
                            container.setMaxConcurrentConsumers(10);
                            container.setConcurrentConsumers(8);
                            container.setAcknowledgeMode(listener.getAcknowledgeMode());
                            container.setMessageListener(listener);
                        })
                );
    }


    @Override
    protected Optional<RabbitConfig> prepareConfig() {
        Boolean enable = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_RABBIT + "enable", Boolean.class);
        String hostname = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_RABBIT + "hostname", String.class);
        Integer port = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_RABBIT + "port", Integer.class);

        if (BooleanUtils.isNotTrue(enable) || StringUtils.isBlank(hostname)
                || Objects.isNull(port)) {
            return Optional.empty();
        }

        String username = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_RABBIT + "username", String.class);
        String password = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_RABBIT + "password", String.class);
        String virtualHost = environment.getProperty(AutoconfigureConstants.PREFIX_MESSAGE_RABBIT + "virtual-host", String.class);

        return Optional.of(
                RabbitConfig.builder()
                        .enable(enable)
                        .hostname(hostname)
                        .port(port)
                        .username(username)
                        .password(password)
                        .virtualHost(virtualHost)
                        .build()
        );
    }
}
