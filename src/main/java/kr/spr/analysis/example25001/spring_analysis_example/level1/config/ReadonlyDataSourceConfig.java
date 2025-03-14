//package kr.spr.analysis.example25001.spring_analysis_example.level1.config;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "kr.spr.analysis.example25001.spring_analysis_example.level2.readonly.repository",
//        entityManagerFactoryRef = "readonlyEntityManagerFactory",
//        transactionManagerRef = "readonlyTransactionManager"
//)
//public class ReadonlyDataSourceConfig {
//
//    @Value("${spring.jpa.readonly.properties.hibernate.hbm2ddl.auto}")
//    private String hbm2ddlAuto;
//
//    @Value("${spring.jpa.readonly.hibernate.dialect}")
//    private String hibernateDialect;
//
//    @Value("${spring.datasource.readonly.jdbc-url}")
//    private String jdbcUrl;
//
//    @Value("${spring.datasource.readonly.username}")
//    private String username;
//
//    @Value("${spring.datasource.readonly.password}")
//    private String password;
//
//    @Value("${spring.datasource.readonly.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.readonly.maximum-pool-size:10}")
//    private int maximumPoolSize;
//
//    @Value("${spring.datasource.readonly.minimum-idle:2}")
//    private int minimumIdle;
//
//    @Value("${spring.datasource.readonly.idle-timeout:30000}")
//    private long idleTimeout;
//
//    @Value("${spring.datasource.readonly.max-lifetime:1800000}")
//    private long maxLifetime;
//
//    @Value("${spring.datasource.readonly.connection-timeout:30000}")
//    private long connectionTimeout;
//
//    @Value("${spring.datasource.readonly.package-name}")
//    private String packageName;
//
//
//    // HikariCP를 사용한 DataSource 설정
//    @Bean(name = "readonlyDataSource")
//    public DataSource readonlyDataSource() {
//
//        HikariConfig hikariConfig = new HikariConfig();
//
//        hikariConfig.setJdbcUrl(jdbcUrl);
//        hikariConfig.setUsername(username);
//        hikariConfig.setPassword(password);
//        hikariConfig.setDriverClassName(driverClassName);
//        hikariConfig.setMaximumPoolSize(maximumPoolSize);
//        hikariConfig.setMinimumIdle(minimumIdle);
//        hikariConfig.setIdleTimeout(idleTimeout);
//        hikariConfig.setMaxLifetime(maxLifetime);
//        hikariConfig.setConnectionTimeout(connectionTimeout);
//
//        return new HikariDataSource(hikariConfig);
//    }
//
//    @Bean(name = "readonlyEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean readonlyEntityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("readonlyDataSource") DataSource dataSource) {
//
//        return builder
//                .dataSource(dataSource)
//                .packages(packageName + ".entity")  // CRUD Entity 패키지
//                .persistenceUnit("readonly")
//                .properties(jpaProperties())
//                .build();
//    }
//
//    private Map<String, Object> jpaProperties() {
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
//        properties.put("hibernate.dialect", hibernateDialect);
//
//        return properties;
//    }
//
//    @Bean(name = "readonlyTransactionManager")
//    public PlatformTransactionManager readonlyTransactionManager(
//            @Qualifier("readonlyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//}
