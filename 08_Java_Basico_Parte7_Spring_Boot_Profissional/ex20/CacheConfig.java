package ex20;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("produtos");
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)                       // no máximo 500 entradas
                .expireAfterWrite(Duration.ofMinutes(10)) // TTL de 10 minutos
                .recordStats());
        return manager;
    }
}
