package cc.cybereflex.infrastructure.autoconfigure.filter;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

import java.util.Objects;
import java.util.Set;

public class AutoConfigurationExclusionFilter implements AutoConfigurationImportFilter {

    private static final Set<String> EXCLUDE_CONFIG =
            Set.of(
                    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
                    "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
                    "org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration",
                    "org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration"
            );


    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] result = new boolean[autoConfigurationClasses.length];

        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            result[i] = !Objects.isNull(autoConfigurationClasses[i]) && !EXCLUDE_CONFIG.contains(autoConfigurationClasses[i]);
        }

        return result;
    }
}
