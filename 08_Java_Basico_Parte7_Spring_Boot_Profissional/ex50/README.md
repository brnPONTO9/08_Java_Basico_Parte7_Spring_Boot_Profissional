# Exercício 50 – Projeto Integrador: Plataforma de Pedidos Profissional

Documento de entrega. O código-base de cada capacidade está nas pastas dos exercícios correspondentes
(veja `LEIA-ME-Parte7.md`) e o projeto da API na Parte 6 (`api-pedidos`).

## Escopo
Monólito modular (ex. 38) com Catálogo, Estoque, Pedido e Pagamento, API REST protegida por JWT (ex. 41),
MySQL com migrações (Flyway), cache (ex. 9/20), eventos com Outbox (ex. 36/44), documentação OpenAPI (ex. 6/15/23),
Docker/Compose (ex. 7/16/24), testes com Testcontainers (ex. 17/25/43) e CI/CD (ex. 10/40/48).

## Como executar
1. `docker compose up --build` (ex. 16) – sobe MySQL e API.
2. Swagger UI: `http://localhost:8080/swagger-ui.html`
3. Testes: `./mvnw verify` (precisa de Docker para Testcontainers).

## Documentos
- `arquitetura.md` – visão da arquitetura e decisões
- `threat-model.md` – ameaças e mitigações
- `rollback.md` – plano de rollback
