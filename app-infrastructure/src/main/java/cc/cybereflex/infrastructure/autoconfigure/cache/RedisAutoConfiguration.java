package cc.cybereflex.infrastructure.autoconfigure.cache;

import cc.cybereflex.infrastructure.autoconfigure.AbstractAutoConfiguration;
import cc.cybereflex.infrastructure.component.RedisCache;
import cc.cybereflex.infrastructure.model.autoconfigure.RedisConfig;
import cc.cybereflex.infrastructure.model.constants.AutoconfigureConstants;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;


public class RedisAutoConfiguration extends AbstractAutoConfiguration<RedisConfig> {

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        super.postProcessBeanDefinitionRegistry(registry);
        try {
            prepareConfig().ifPresent(config -> {
                RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
                standaloneConfig.setDatabase(config.getDatabase());
                standaloneConfig.setHostName(config.getHostname());
                standaloneConfig.setPort(config.getPort());
                standaloneConfig.setUsername(config.getUsername());
                standaloneConfig.setPassword(config.getPassword());


                GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
                poolConfig.setMaxIdle(10);
                poolConfig.setMinIdle(10);
                poolConfig.setMaxWait(Duration.ofSeconds(10));
                poolConfig.setMaxTotal(20);

                LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                        .commandTimeout(Duration.ofSeconds(30))
                        .shutdownTimeout(Duration.ZERO)
                        .poolConfig(poolConfig)
                        .build();

                LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(standaloneConfig, clientConfig);

                StringRedisTemplate redisTemplate = new StringRedisTemplate();
                redisTemplate.setConnectionFactory(redisConnectionFactory);

                registerBean(AutoconfigureConstants.REDIS_CACHE_BEAN_NAME, RedisCache.class, new RedisCache(redisTemplate));
            });
        }catch (Exception e){
            logger.error("redis autoconfigure failed，{}", e.getMessage());
            throw new BeanCreationException(e.getMessage());
        }
    }

    @Override
    protected Optional<RedisConfig> prepareConfig() {

        Boolean enable = environment.getProperty(AutoconfigureConstants.PREFIX_CACHE_REDIS + "enable", Boolean.class);
        String hostname = environment.getProperty(AutoconfigureConstants.PREFIX_CACHE_REDIS + "hostname", String.class);
        Integer port = environment.getProperty(AutoconfigureConstants.PREFIX_CACHE_REDIS + "port", Integer.class);
        Integer database = environment.getProperty(AutoconfigureConstants.PREFIX_CACHE_REDIS + "database", Integer.class);
        if (BooleanUtils.isNotTrue(enable)
                || StringUtils.isBlank(hostname)
                || Objects.isNull(port)
                || Objects.isNull(database)) {

            return Optional.empty();
        }

        String username = environment.getProperty(AutoconfigureConstants.PREFIX_CACHE_REDIS + "username", String.class);
        String password = environment.getProperty(AutoconfigureConstants.PREFIX_CACHE_REDIS + "password", String.class);

        return Optional.of(
                RedisConfig.builder()
                        .hostname(hostname)
                        .username(username)
                        .password(password)
                        .database(database)
                        .port(port)
                        .build()
        );
    }
}
