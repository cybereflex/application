package cc.cybereflex.infrastructure.autoconfigure;

import cc.cybereflex.infrastructure.model.autoconfigure.BaseConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAutoConfiguration<T extends BaseConfig> implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractAutoConfiguration.class);

    protected Environment environment;
    protected BeanDefinitionRegistry registry;
    protected ConfigurableListableBeanFactory beanFactory;
    protected ApplicationContext applicationContext;

    protected abstract Optional<T> prepareConfig();

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected <I> void registerBean(String beanName, Class<I> clazz, I instance) {
        registry.registerBeanDefinition(
                beanName,
                BeanDefinitionBuilder
                        .genericBeanDefinition(clazz, () -> instance)
                        .getBeanDefinition()
        );
    }

    protected <C> Optional<List<C>> getBeans(Class<C> clazz) {
        String[] beanNames = applicationContext.getBeanNamesForType(clazz);

        if (ArrayUtils.isEmpty(beanNames)) {
            return Optional.empty();
        }

        return Optional.of(
                Arrays.stream(beanNames)
                        .map(it -> applicationContext.getBean(it, clazz))
                        .toList()
        );

    }
}
