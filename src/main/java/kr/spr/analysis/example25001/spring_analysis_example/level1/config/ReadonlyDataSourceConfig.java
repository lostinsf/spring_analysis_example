package kr.spr.analysis.example25001.spring_analysis_example.level1.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
        basePackages = "kr.spr.analysis.example25001.spring_analysis_example.level2.readonly.repository",
        entityManagerFactoryRef = "readonlyEntityManagerFactory",
        transactionManagerRef = "readonlyTransactionManager"
)
public class ReadonlyDataSourceConfig {

    @Value("${spring.datasource.readonly.jdbc-url:#{null}}")
    private String jdbcUrl;
    @Value("${spring.datasource.readonly.username:#{null}}")
    private String username;
    @Value("${spring.datasource.readonly.password:#{null}}")
    private String password;
    @Value("${spring.datasource.readonly.driver-class-name:#{null}}")
    private String driverClassName;
    @Value("${spring.datasource.readonly.package-name:#{null}}")
    private String packageName;
    @Value("${spring.jpa.readonly.hibernate.dialect:#{null}}")
    private String hibernateType;

    // 읽기 전용 DataSource 설정
    @Bean(name = "readonlyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.readonly")
    public DataSource readonlyDataSource() {

        DataSource dataSource = DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();

        return dataSource;
    }

    @Bean(name = "readonlyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean readonlyEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("readonlyDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", hibernateType); // ✅ Hibernate Dialect 추가

        return builder
                .dataSource(dataSource)
                .packages(packageName + ".entity")
                .persistenceUnit("readonly")
                .properties(properties)
                .build();
    }

    @Bean(name = "readonlyTransactionManager")
    public PlatformTransactionManager readonlyTransactionManager(
            @Qualifier("readonlyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
