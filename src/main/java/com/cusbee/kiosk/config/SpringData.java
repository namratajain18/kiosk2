package com.cusbee.kiosk.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Throwables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.cusbee.kiosk.repository")
public class SpringData extends WebMvcConfigurerAdapter {

    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String MAX_SIZE = "hibernate.c3p0.max_size";
    private static final String ENTITYMANAGER_PACKAGES_TO_SCAN = "com.cusbee.kiosk.entity";

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(500000000);
        multipartResolver.setMaxInMemorySize(500000000);
        return multipartResolver;
    }

    @Bean
    public DataSource dataSource() {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            //due to my local issue. Set up to the remote server.
            //dataSource.setUrl("jdbc:mysql://165.227.111.167:3306/kiosk?autoReconnect=true&amp;createDatabaseIfNotExist=true&amp;");
            //dataSource.setUrl("jdbc:mysql://138.197.127.24:3306/kiosk?autoReconnect=true&amp;createDatabaseIfNotExist=true&amp;");
            dataSource.setUrl("jdbc:mysql://localhost:3306/kiosk?autoReconnect=true&amp;createDatabaseIfNotExist=true&amp;");
            dataSource.setUsername("root");
            dataSource.setPassword("wsxzasdc"); 
            //dataSource.setPassword("wsxzasdc"); 
            //dataSource.setPassword("hpj15n"); //
            return dataSource;
        } catch (Exception ex) {
            Throwable rootCause = Throwables.getRootCause(ex);
            throw Throwables.propagate(rootCause);
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
        em.setDataSource(dataSource());
        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        vendor.setShowSql(false);
        em.setJpaVendorAdapter(vendor);
        em.setJpaProperties(hibProperties());
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties hibProperties() {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.put(HIBERNATE_HBM2DDL_AUTO, "update");
        properties.put(HIBERNATE_SHOW_SQL, "false");
        properties.put(MAX_SIZE, 25);
        return properties;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
            .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
    }
}
