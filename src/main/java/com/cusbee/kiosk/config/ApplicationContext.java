package com.cusbee.kiosk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */
@Configuration
//@EnableCaching
@ComponentScan(basePackages = {"com.cusbee.kiosk"})
@EnableWebMvc
@Import({ Thymeleaf.class, Swagger.class})
public class ApplicationContext extends WebMvcConfigurerAdapter {

    public static org.springframework.context.ApplicationContext context;

    @Nullable
    public static Optional<org.springframework.context.ApplicationContext> getContext() {
        return Optional.ofNullable(context);
    }

    @EventListener({ContextRefreshedEvent.class})
    public void contextRefreshedEvent(ContextRefreshedEvent event) {
        context = event.getApplicationContext();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
       // mailSender.setUsername("noreply.patrawalnutcreek@gmail.com");
       // mailSender.setPassword("PatraWC.");
        mailSender.setUsername("noreply.buckhorngrill@gmail.com");
        //mailSender.setUsername("noreply.tritipgrill@gmail.com");
        mailSender.setPassword("wsxzasdc");
        Properties prop = mailSender.getJavaMailProperties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.debug", "true");
        return mailSender;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(500000000);
        multipartResolver.setMaxInMemorySize(500000000);
        return multipartResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //Used for displaying SWAGGER UI
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars*")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        //Used for free access to the site and resources from webapp
        registry.addResourceHandler("/site/**")
                .addResourceLocations("WEB-INF/resources/site/");

        /*registry.addResourceHandler("*//**")
                .addResourceLocations("WEB-INF/resources/");*/
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://138.197.127.24:8080")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false).maxAge(3600);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


}
