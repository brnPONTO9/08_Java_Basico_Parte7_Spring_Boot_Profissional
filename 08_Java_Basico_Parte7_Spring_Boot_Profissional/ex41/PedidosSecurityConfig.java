package ex41;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/** Exercício 41: API de pedidos protegida por JWT, com scopes de leitura e escrita. */
@Configuration
public class PedidosSecurityConfig {

    @Bean
    SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/actuator/health/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasAuthority("SCOPE_pedidos:read")
                .requestMatchers(HttpMethod.POST, "/api/pedidos/**").hasAuthority("SCOPE_pedidos:write")
                .requestMatchers(HttpMethod.PUT, "/api/pedidos/**").hasAuthority("SCOPE_pedidos:write")
                .requestMatchers(HttpMethod.DELETE, "/api/pedidos/**").hasAuthority("SCOPE_pedidos:write")
                .anyRequest().denyAll())  // o que não foi liberado explicitamente é negado
            .oauth2ResourceServer(o -> o.jwt(j -> { }));
        return http.build();
    }
}
