package com.cusbee.kiosk.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

import com.cusbee.kiosk.config.security.web.HeaderFilter;
import com.cusbee.kiosk.config.security.web.KioskBasicAuthenticationFilter;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContext.class);
        rootContext.setDisplayName("KIOSK");

        servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.addListener(new RequestContextListener());
        servletContext.addListener(new Log4jConfigListener());
        servletContext.addListener(new HttpSessionEventPublisher());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");

        // Encoding filter
        FilterRegistration encodingFilter = servletContext.addFilter("encodingFilter", new CharacterEncodingFilter());
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");

         // Kiosk Mobile Client filter
         FilterRegistration.Dynamic mobileClientFilter = servletContext.addFilter("mobClientFilter", KioskBasicAuthenticationFilter.class);
         mobileClientFilter.addMappingForUrlPatterns(null, false, "/*");

        // Kiosk Default Client filter
        FilterRegistration.Dynamic defaultClientFilter = servletContext.addFilter("defClientFilter", KioskBasicAuthenticationFilter.class);
        defaultClientFilter.addMappingForUrlPatterns(null, false, "/*");

        // Spring Security Delegate filter
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        FilterRegistration.Dynamic security = servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy());

        security.addMappingForUrlPatterns(dispatcherTypes, true, "/*");

        // CORS filter
        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFilter", HeaderFilter.class);
        corsFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
