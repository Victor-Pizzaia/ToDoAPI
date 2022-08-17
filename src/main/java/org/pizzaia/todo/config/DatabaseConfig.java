package org.pizzaia.todo.config;

import org.pizzaia.todo.util.SecretReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories("org.pizzaia.todo.repository")
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        Map<String, String> secretData = SecretReader.read();
        System.out.println(secretData);
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(secretData.get("driverClassName"));
        ds.setUrl(secretData.get("url"));
        ds.setUsername(secretData.get("username"));
        ds.setPassword(secretData.get("password"));
        return ds;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("org.pizzaia.todo.model");
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
