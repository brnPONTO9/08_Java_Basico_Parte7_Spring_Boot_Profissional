# Exercício 42 – Integrar Keycloak (OIDC)

1. Suba o Keycloak: `docker run -p 8081:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=<defina> quay.io/keycloak/keycloak:25.0 start-dev`
2. Crie o realm `pedidos`, o client `api-pedidos` e os scopes `pedidos:read` / `pedidos:write`.
3. Configure a API com o `application-oidc.yml` (apenas valida assinatura via JWKS, issuer e audience).
4. Obtenha o token no Keycloak (fluxo password só em laboratório; em produção Authorization Code + PKCE) e envie `Authorization: Bearer ...`.
5. **O que sai da API:** `AuthController`, `JwtEncoder`, chave privada de assinatura. Agora só existe a chave pública (JWKS).
6. Mapeamento de papéis do Keycloak (`realm_access.roles`) exige um `JwtAuthenticationConverter` próprio, como no ex. 13.
