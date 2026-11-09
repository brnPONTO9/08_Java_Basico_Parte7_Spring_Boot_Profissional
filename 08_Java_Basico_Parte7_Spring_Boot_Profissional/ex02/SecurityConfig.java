package ex02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // API sem cookie de sessão (ver ex. 12)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll() // aberto
                .anyRequest().authenticated())             // todo o resto exige autenticação
            .httpBasic(basic -> { });
        return http.build();
    }
}
