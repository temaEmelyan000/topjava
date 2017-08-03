package ru.javawebinar.topjava.config;


import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:db/postgres.properties")
//@PropertySource("classpath:db/hsqldb.properties")
@ComponentScan({/*"ru.**.jdbc", */"ru.**.jpa"})
public class SpringDBConfig {
    private @Value("${database.url}")
    String url;

    private @Value("${database.username}")
    String user;

    private @Value("${database.password}")
    String password;

    private @Value("${database.driverClassName}")
    String driverName;

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("ru.**.model");

        entityManagerFactoryBean.setJpaPropertyMap(new HashMap<String, Boolean>() {
            {
                put(AvailableSettings.FORMAT_SQL, true);
                put(AvailableSettings.USE_SQL_COMMENTS, true);
            }
        });

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        return entityManagerFactoryBean;
    }


    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);

        return jpaTransactionManager;
    }


//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(dataSource());
//    }
//
//    @Bean
//    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
//        return new NamedParameterJdbcTemplate(dataSource());
//    }
}
