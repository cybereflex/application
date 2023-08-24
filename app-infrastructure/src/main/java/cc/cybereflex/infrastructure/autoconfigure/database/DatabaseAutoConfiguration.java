package cc.cybereflex.infrastructure.autoconfigure.database;

import cc.cybereflex.infrastructure.autoconfigure.AbstractAutoConfiguration;
import cc.cybereflex.infrastructure.model.autoconfigure.DatabaseConfig;
import cc.cybereflex.infrastructure.model.constants.AutoconfigureConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatabaseAutoConfiguration extends AbstractAutoConfiguration<DatabaseConfig> {

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        super.postProcessBeanDefinitionRegistry(registry);

        try {
            prepareConfig().ifPresent(cfg ->
                    cfg.getDatasource().forEach(config -> {
                        try {
                            //registry dataSource
                            HikariDataSource hikariDataSource = getHikariDataSource(config);
                            registerBean(config.getDataSourceBeanName(), DataSource.class, hikariDataSource);

                            if (StringUtils.isNotBlank(config.getJdbcTemplateBeanName())){
                                //registry jdbc template
                                JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);
                                registerBean(config.getJdbcTemplateBeanName(), JdbcTemplate.class, jdbcTemplate);
                            }

                            PlatformTransactionManager transactionManager = new DataSourceTransactionManager(hikariDataSource);
                            if (StringUtils.isNotBlank(config.getTransactionManagerBeanName())){
                                //registry transaction manager
                                registerBean(config.getTransactionManagerBeanName(), PlatformTransactionManager.class, transactionManager);
                            }

                            //registry transaction template
                            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                            registerBean(config.getTransactionTemplateBeanName(), TransactionTemplate.class, transactionTemplate);

                            //build sqlSessionFactory
                            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
                            sqlSessionFactoryBean.setDataSource(hikariDataSource);
                            List<Resource> resources = config.getMapperLocations().stream()
                                    .flatMap(it -> {
                                        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                                        try {
                                            return Arrays.stream(resolver.getResources(it));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .toList();
                            sqlSessionFactoryBean.setMapperLocations(resources.toArray(new Resource[0]));
                            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();

                            if (Objects.nonNull(sqlSessionFactory)) {
                                //registry sqlSessionTemplate
                                registerBean(config.getSqlSessionTemplateBeanName(), SqlSessionTemplate.class, new SqlSessionTemplate(sqlSessionFactory));

                                //execute mybatis mapper scan
                                MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
                                mapperScannerConfigurer.setSqlSessionTemplateBeanName(config.getSqlSessionTemplateBeanName());
                                mapperScannerConfigurer.setBasePackage(config.getBasePackages());
                                mapperScannerConfigurer.postProcessBeanDefinitionRegistry(registry);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
        } catch (Exception e) {
            logger.error("jdbc database autoconfigure failed，{}", e.getMessage());
            throw new BeanCreationException(e.getMessage());
        }
    }

    private HikariDataSource getHikariDataSource(DatabaseConfig.DataSourceConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setJdbcUrl(config.getJdbcURL());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }


    @Override
    protected Optional<DatabaseConfig> prepareConfig() {

        Boolean enable = environment.getProperty(AutoconfigureConstants.PREFIX_DATABASE + "enable", Boolean.class);
        Integer count = environment.getProperty(AutoconfigureConstants.PREFIX_DATABASE + "count", Integer.class);
        if (BooleanUtils.isNotTrue(enable) || Objects.isNull(count) || Objects.equals(0, count)) {
            return Optional.empty();
        }

        Set<DatabaseConfig.DataSourceConfig> dataSourceConfigs = IntStream.range(0, count)
                .mapToObj(i -> {
                    String prefix = String.format(AutoconfigureConstants.PREFIX_DATABASE + "datasource[%d].", i);

                    String jdbcURL = environment.getProperty(prefix + "jdbc-url", String.class);
                    String username = environment.getProperty(prefix + "username", String.class);
                    String password = environment.getProperty(prefix + "password", String.class);
                    String driverClassName = environment.getProperty(prefix + "driver-class-name", String.class);
                    Integer mapperLocationCount = environment.getProperty(prefix + "mapper-location-count", Integer.class);
                    String basePackages = environment.getProperty(prefix + "base-packages", String.class);
                    String dataSourceBeanName = environment.getProperty(prefix + "data-source-bean-name", String.class);
                    String sqlSessionTemplateBeanName = environment.getProperty(prefix + "sql-session-template-bean-name", String.class);
                    String transactionTemplateBeanName = environment.getProperty(prefix + "transaction-template-bean-name", String.class);

                    //ignore invalid config
                    if (StringUtils.isBlank(jdbcURL) || StringUtils.isBlank(driverClassName)
                            || StringUtils.isBlank(basePackages) || StringUtils.isBlank(dataSourceBeanName)
                            || StringUtils.isBlank(sqlSessionTemplateBeanName) || StringUtils.isBlank(transactionTemplateBeanName)) {

                        return null;
                    }

                    String jdbcTemplateBeanName = environment.getProperty(prefix + "jdbc-template-bean-name", String.class);
                    String transactionManagerBeanName = environment.getProperty(prefix + "transaction-manager-bean-name", String.class);


                    DatabaseConfig.DataSourceConfig dataSourceConfig = DatabaseConfig.DataSourceConfig.builder()
                            .jdbcURL(jdbcURL)
                            .username(username)
                            .password(password)
                            .driverClassName(driverClassName)
                            .basePackages(basePackages)
                            .dataSourceBeanName(dataSourceBeanName)
                            .jdbcTemplateBeanName(jdbcTemplateBeanName)
                            .sqlSessionTemplateBeanName(sqlSessionTemplateBeanName)
                            .transactionTemplateBeanName(transactionTemplateBeanName)
                            .transactionManagerBeanName(transactionManagerBeanName)
                            .mapperLocationCount(mapperLocationCount)
                            .build();

                    if (Objects.nonNull(mapperLocationCount) && mapperLocationCount > 0) {
                        Set<String> mapperLocations = IntStream.range(0, mapperLocationCount)
                                .mapToObj(j -> environment.getProperty(String.format(prefix + "mapper-locations[%d]", j), String.class))
                                .collect(Collectors.toSet());

                        dataSourceConfig.setMapperLocationCount(mapperLocationCount);
                        dataSourceConfig.setMapperLocations(mapperLocations);
                    }


                    return dataSourceConfig;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return Optional.of(
                DatabaseConfig.builder()
                        .enable(enable)
                        .count(count)
                        .datasource(dataSourceConfigs)
                        .build()
        );
    }


}
