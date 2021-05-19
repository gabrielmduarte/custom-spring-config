package com.gabrielduarte.customconfigpoc.config;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:mysql.properties")
@ConditionalOnClass(DataSource.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class MySQLConfig {

    private final Environment env;

    public MySQLConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @ConditionalOnBean(name = "dataSource")
    @ConditionalOnMissingBean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        if (additionalProperties() != null) {
            em.setJpaProperties(additionalProperties());
        }

        return em;
    }

    @Bean(name = "dataSource")
    @ConditionalOnProperty(name = "usemysql", havingValue = "local")
    @ConditionalOnMissingBean
    private DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/myDb?createDatabaseIfNotExist=true&&serverTimezone=UTC");
        dataSource.setUsername("mysqluser");
        dataSource.setPassword("mysqlpass");

        return dataSource;
    }

    @Bean(name = "dataSource")
    @ConditionalOnProperty(name = "usemysql", havingValue = "custom")
    @ConditionalOnMissingBean
    private DataSource dataSource2() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(env.getProperty("mysql.url") != null ? env.getProperty("mysql.url") : "");
        dataSource.setUsername(env.getProperty("mysql.user") != null ? env.getProperty("mysql.user") : "");
        dataSource.setPassword(env.getProperty("mysql.pass") != null ? env.getProperty("mysql.pass") : "");

        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean(type = "JpaTransactionManager")
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }

    @ConditionalOnResource(
        resources = "classpath:mysql.properties"
    )
    Properties additionalProperties() {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("mysql-hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("mysql-hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql",
                                            env.getProperty("mysql-hibernate.show_sql") != null
                                                    ? env.getProperty("mysql-hibernate.show_sql") : "false");

        return hibernateProperties;
    }

}
