package ex11;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("local") // só vale quando o profile "local" está ativo
public class CorsConfig {

    @Bean
    WebMvcConfigurer cors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000") // somente esta origem, nunca "*" com credenciais
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
// Com Spring Security, também habilite:  http.cors(Customizer.withDefaults())
