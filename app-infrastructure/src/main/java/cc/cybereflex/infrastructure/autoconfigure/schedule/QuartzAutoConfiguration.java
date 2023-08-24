package cc.cybereflex.infrastructure.autoconfigure.schedule;

import cc.cybereflex.infrastructure.autoconfigure.AbstractAutoConfiguration;
import cc.cybereflex.infrastructure.component.QuartzScheduleManager;
import cc.cybereflex.infrastructure.model.autoconfigure.QuartzConfig;
import cc.cybereflex.infrastructure.model.constants.AutoconfigureConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.quartz.JobStoreType;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Optional;

public class QuartzAutoConfiguration extends AbstractAutoConfiguration<QuartzConfig> {

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        super.postProcessBeanDefinitionRegistry(registry);


        prepareConfig().ifPresent(it -> {

            try {
                SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

                if (StringUtils.equals("jdbc", it.getJobStoreType())) {
                    HikariConfig hikariConfig = new HikariConfig();
                    hikariConfig.setUsername(it.getUsername());
                    hikariConfig.setPassword(it.getPassword());
                    hikariConfig.setJdbcUrl(it.getJdbcUrl());
                    hikariConfig.setDriverClassName(it.getDriverClassName());
                    HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
                    schedulerFactoryBean.setDataSource(hikariDataSource);

                    JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager(hikariDataSource);
                    schedulerFactoryBean.setTransactionManager(jdbcTransactionManager);

                    QuartzProperties properties = new QuartzProperties();
                    properties.setJobStoreType(JobStoreType.JDBC);
                    properties.getJdbc().setInitializeSchema(DatabaseInitializationMode.ALWAYS);

                    new QuartzDataSourceScriptDatabaseInitializer(hikariDataSource, properties).initializeDatabase();
                }

                schedulerFactoryBean.afterPropertiesSet();
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                scheduler.start();

                registerBean(AutoconfigureConstants.QUARTZ_SCHEDULE_BEAN_NAME, Scheduler.class, scheduler);
                registerBean(AutoconfigureConstants.QUARTZ_SCHEDULE_MANAGER_BEAN_NAME, QuartzScheduleManager.class, new QuartzScheduleManager(scheduler));
            } catch (Exception e) {
                logger.error("quartz autoconfigure failed", e);
                throw new BeanCreationException(e.getMessage());
            }
        });
    }

    @Override
    protected Optional<QuartzConfig> prepareConfig() {
        Boolean enable = environment.getProperty(AutoconfigureConstants.PREFIX_SCHEDULE_QUARTZ + "enable", Boolean.class);
        String jobStoreType = environment.getProperty(AutoconfigureConstants.PREFIX_SCHEDULE_QUARTZ + "job-store-type", String.class);
        String jdbcURL = environment.getProperty(AutoconfigureConstants.PREFIX_SCHEDULE_QUARTZ + "jdbc-url", String.class);
        String username = environment.getProperty(AutoconfigureConstants.PREFIX_SCHEDULE_QUARTZ + "username", String.class);
        String password = environment.getProperty(AutoconfigureConstants.PREFIX_SCHEDULE_QUARTZ + "password", String.class);
        String driverClassName = environment.getProperty(AutoconfigureConstants.PREFIX_SCHEDULE_QUARTZ + "driver-class-name", String.class);

        if (BooleanUtils.isNotTrue(enable)) {
            return Optional.empty();
        }


        if (StringUtils.equals("jdbc", jobStoreType)
                && (StringUtils.isBlank(jdbcURL)
                || StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(driverClassName))
        ) {
            return Optional.empty();
        }


        return Optional.of(
                QuartzConfig.builder()
                        .enable(enable)
                        .jobStoreType(jobStoreType)
                        .jdbcUrl(jdbcURL)
                        .username(username)
                        .password(password)
                        .driverClassName(driverClassName)
                        .build()
        );
    }

}
