# Arquitetura

```
 Cliente --HTTPS--> [Gateway/Ingress] --> API (Spring Boot, stateless, JWT)
                                           |-- módulo Catálogo
                                           |-- módulo Estoque
                                           |-- módulo Pedido ---> Outbox (mesma transação) --> Broker (RabbitMQ/Kafka)
                                           '-- módulo Pagamento --> Gateway de pagamento (RestClient + retry + circuit breaker)
                       MySQL (Flyway)   Redis/Caffeine (cache)   Provedor OIDC (Keycloak)
```

Decisões principais:
| Decisão | Motivo |
|---------|--------|
| Monólito modular | Menor custo operacional; limites prontos para extração futura |
| JWT curto (15 min) + refresh com rotação | Reduz janela de abuso de token roubado |
| Provedor OIDC externo | A API não guarda senha nem assina token |
| Outbox + consumidor idempotente + DLQ | Entrega confiável sem transação distribuída |
| Migrações expand/contract | Rolling deploy sem downtime |
| Health liveness/readiness separados | Orquestrador reinicia só quando faz sentido |
