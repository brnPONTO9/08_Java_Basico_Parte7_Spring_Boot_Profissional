# Threat model (STRIDE resumido)

| Ameaça | Exemplo | Mitigação |
|--------|---------|-----------|
| **S**poofing (falsificar identidade) | Token forjado ou de outro destinatário | Validar assinatura, `iss`, `aud`, `exp`; chaves via JWKS; HTTPS |
| **T**ampering (adulterar dados) | Alterar preço no corpo da requisição | Calcular preço no servidor; Bean Validation; JWT assinado |
| **R**epudiation (negar autoria) | "Eu não fiz esse pedido" | Logs de auditoria com usuário e `correlationId` (sem dados sensíveis) |
| **I**nformation disclosure | Vazamento por Actuator, erro detalhado ou log com token | Expor só `health`/`info`; mensagens de erro genéricas; nunca logar token/senha; segredos em variáveis/secret manager |
| **D**enial of service | Rajada de requisições | Rate limit no gateway, timeouts, pool de conexões limitado, circuit breaker |
| **E**levation of privilege | Usuário comum chama rota de admin | Autorização por scope/role em cada rota; `anyRequest().denyAll()`; testes 401/403 |

Outros: SQL injection (somente PreparedStatement/JPA), dependências vulneráveis (Dependabot / `mvn dependency-check`),
imagem Docker não-root e mínima, segredos nunca no Git.
