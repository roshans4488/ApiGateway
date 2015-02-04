package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.GemfireCacheManager;

import com.gemstone.gemfire.cache.Cache;
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableCaching
@EnableGemfireRepositories
public class Application {

	
	@Bean
    CacheFactoryBean cacheFactoryBean() {
        return new CacheFactoryBean();
    }
	
	@Bean
    LocalRegionFactoryBean<Integer, Integer> localRegionFactoryBean(final Cache cache) {
        return new LocalRegionFactoryBean<Integer, Integer>() {{
            setCache(cache);
            setName("hello");
        }};
    }

    @Bean
    GemfireCacheManager cacheManager(final Cache gemfireCache) {
        return new GemfireCacheManager() {{
            setCache(gemfireCache);
        }};
    }
   
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

} 