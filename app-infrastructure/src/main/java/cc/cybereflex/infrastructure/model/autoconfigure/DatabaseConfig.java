package cc.cybereflex.infrastructure.model.autoconfigure;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DatabaseConfig extends BaseConfig {

    private Integer count;

    private Set<DataSourceConfig> datasource;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DataSourceConfig {
        /**
         * JDBC schema
         */
        private String jdbcURL;
        /**
         * 数据库用户名
         */
        private String username;
        /**
         * 数据库密码
         */
        private String password;
        /**
         * driver name
         */
        private String driverClassName;

        /**
         * data source bean name
         */
        private String dataSourceBeanName;

        /**
         * mybatis sql session template bean name
         */
        private String sqlSessionTemplateBeanName;

        /**
         * transaction template bean name
         */
        private String transactionTemplateBeanName;

        /**
         * transaction manager bean name
         */
        private String transactionManagerBeanName;

        /**
         * jdbc template bean name
         */
        private String jdbcTemplateBeanName;

        /**
         * mapper interface 包相对引用
         */
        private String basePackages;

        /**
         * mapper xml 文件路径数量
         */
        private Integer mapperLocationCount;

        /**
         * mapper xml 文件路径
         */
        private Set<String> mapperLocations;
    }
}
