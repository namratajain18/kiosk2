package com.cusbee.kiosk.config;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */

//@Configuration
//@EnableCaching
//@ComponentScan(basePackages = {"com.cusbee.kiosk"})
public class EhCache {
    //todo: configure cache after services will be added
    /*private static final Logger logger =
            LoggerFactory.getLogger(EhCache.class);
    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }*/
}
