package ex14;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Login de LABORATÓRIO: emite o token localmente. Em produção use um provedor OIDC (ex. 42). */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.usuario(), req.senha())); // lança 401 se inválido
        Instant agora = Instant.now();
        String scope = String.join(" ", auth.getAuthorities().stream().map(a -> a.getAuthority()).toList());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("api-pedidos")
                .subject(auth.getName())
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(900))
                .claim("scope", scope)
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return Map.of("access_token", token, "token_type", "Bearer");
    }
}
