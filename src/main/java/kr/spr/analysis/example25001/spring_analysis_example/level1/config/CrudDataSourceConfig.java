package kr.spr.analysis.example25001.spring_analysis_example.level1.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository",
        entityManagerFactoryRef = "crudEntityManagerFactory",
        transactionManagerRef = "crudTransactionManager"
)
public class CrudDataSourceConfig {

    @Value("${spring.jpa.crud.properties.hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${spring.jpa.crud.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${spring.datasource.crud.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.crud.username}")
    private String username;

    @Value("${spring.datasource.crud.password}")
    private String password;

    @Value("${spring.datasource.crud.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.crud.maximum-pool-size:10}")
    private int maximumPoolSize;

    @Value("${spring.datasource.crud.minimum-idle:2}")
    private int minimumIdle;

    @Value("${spring.datasource.crud.idle-timeout:30000}")
    private long idleTimeout;

    @Value("${spring.datasource.crud.max-lifetime:1800000}")
    private long maxLifetime;

    @Value("${spring.datasource.crud.connection-timeout:30000}")
    private long connectionTimeout;

    @Value("${spring.datasource.crud.package-name}")
    private String packageName;


    // HikariCP를 사용한 DataSource 설정
    @Bean(name = "crudDataSource")
    public DataSource crudDataSource() {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setConnectionTimeout(connectionTimeout);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "crudEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean crudEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("crudDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages(packageName + ".entity")  // CRUD Entity 패키지
                .persistenceUnit("crud")
                .properties(jpaProperties())
                .build();
    }

    private Map<String, Object> jpaProperties() {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        properties.put("hibernate.dialect", hibernateDialect);

        return properties;
    }

    @Bean(name = "crudTransactionManager")
    public PlatformTransactionManager crudTransactionManager(
            @Qualifier("crudEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

}
