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
        basePackages = "kr.spr.analysis.example25001.spring_analysis_example.level2.crud.repository",
        entityManagerFactoryRef = "crudEntityManagerFactory",
        transactionManagerRef = "crudTransactionManager"
)
public class CrudDataSourceConfig {

    @Value("${spring.datasource.crud.jdbc-url:#{null}}")
    private String jdbcUrl;
    @Value("${spring.datasource.crud.username:#{null}}")
    private String username;
    @Value("${spring.datasource.crud.password:#{null}}")
    private String password;
    @Value("${spring.datasource.crud.driver-class-name:#{null}}")
    private String driverClassName;
    @Value("${spring.datasource.crud.package-name:#{null}}")
    private String packageName;
    @Value("${spring.jpa.crud.hibernate.dialect:#{null}}")
    private String hibernateType;

    // CRUD 가능 DataSource 설정
    @Bean(name = "crudDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.crud")
    public DataSource crudDataSource() {

        DataSource dataSource = DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();

        return dataSource;

    }

    @Bean(name = "crudEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean crudEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("crudDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", hibernateType); // ✅ Hibernate Dialect 추가

        return builder
                .dataSource(dataSource)
                .packages(packageName + ".entity")  // CRUD Entity 패키지
                .persistenceUnit("crud")
                .properties(properties)
                .build();
    }

    @Bean(name = "crudTransactionManager")
    public PlatformTransactionManager crudTransactionManager(
            @Qualifier("crudEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
