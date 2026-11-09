package ex21;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private static final Duration VALIDADE = Duration.ofMinutes(15); // access token CURTO
    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) { this.encoder = encoder; }

    public String emitirAccessToken(String usuario, List<String> scopes) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://auth.minhaloja.exemplo")
                .audience(List.of("api-pedidos"))     // para quem o token é destinado
                .subject(usuario)
                .issuedAt(agora)
                .expiresAt(agora.plus(VALIDADE))
                .claim("scope", String.join(" ", scopes))
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
