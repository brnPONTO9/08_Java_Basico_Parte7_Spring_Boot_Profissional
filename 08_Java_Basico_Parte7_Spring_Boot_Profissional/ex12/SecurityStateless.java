package ex12;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Por que não usa sessão HTTP: em uma API stateless cada requisição leva o token (Authorization: Bearer ...)
 * e o servidor valida sozinho, sem guardar nada em memória. Vantagens: escala horizontalmente sem "sessão
 * grudenta" ou Redis de sessão, funciona bem com várias instâncias e mobile/SPA. Sem cookie de sessão, o
 * ataque CSRF (que depende do navegador enviar o cookie sozinho) deixa de se aplicar, por isso csrf pode ser desligado.
 */
@Configuration
public class SecurityStateless {

    @Bean
    SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a.anyRequest().authenticated())
            .oauth2ResourceServer(o -> o.jwt(j -> { }));
        return http.build();
    }
}
