package cc.cybereflex.infrastructure;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@SuppressWarnings("unused")
@Configuration
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

    public static boolean isSingleton(String beanName) {
        return applicationContext.isSingleton(beanName);
    }

    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }
}
