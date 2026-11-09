package ex13;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/*
 * A claim "scope" (ex.: "pedidos:read pedidos:write") vira authorities SCOPE_pedidos:read, SCOPE_pedidos:write.
 * Este já é o comportamento padrão do Spring; aqui deixamos explícito para poder customizar (prefixo/claim).
 */
@Configuration
public class ScopeAuthorities {

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("scope");
        authorities.setAuthorityPrefix("SCOPE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }
}
// Uso:  .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasAuthority("SCOPE_pedidos:read")
