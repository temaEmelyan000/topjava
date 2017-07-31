package ru.javawebinar.topjava.springConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.javawebinar.topjava.util.DbPopulator;

@Configuration
@PropertySource("classpath:db/postgres.properties")
@ComponentScan("ru.**.jdbc")
public class SpringDBConfig {
    private @Value("${database.url}")
    String url;
    private @Value("${database.username}")
    String user;
    private @Value("${database.password}")
    String password;

    @Bean
    public DriverManagerDataSource DataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(DataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(DataSource());
    }

    @Bean
    public DbPopulator dbPopulator() {
        return new DbPopulator(DataSource(), "classpath:db/initDB.sql", "classpath:db/populateDB.sql");
    }
}
