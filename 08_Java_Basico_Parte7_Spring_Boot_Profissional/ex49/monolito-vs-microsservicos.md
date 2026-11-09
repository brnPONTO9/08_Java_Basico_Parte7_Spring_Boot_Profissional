# Exercício 49 – Monólito modular x microsserviços (domínio: loja de pedidos)

| Critério | Monólito modular | Microsserviços |
|----------|------------------|----------------|
| Deploy | 1 artefato, 1 pipeline | N artefatos e N pipelines |
| Operação | 1 app + 1 banco: logs e métricas simples | Rede, descoberta de serviço, tracing distribuído, gateway, vários bancos |
| Consistência | Transação ACID local (pedido + estoque juntos) | Consistência eventual: Saga, Outbox, idempotência, compensações |
| Latência | Chamada de método | Chamada de rede (latência, timeout, retry, circuit breaker) |
| Escala | Escala tudo junto | Escala só o que precisa (ex.: pagamentos) |
| Autonomia de times | Times compartilham código e release | Times independentes, releases independentes |
| Custo (infra + pessoas) | Baixo | Alto (plataforma, observabilidade, maturidade DevOps) |
| Testes | Integração simples | Contrato entre serviços + ambiente complexo |
| Risco típico | "Big ball of mud" se os módulos não forem respeitados | "Monólito distribuído": acoplamento sem os benefícios |

## Custo de operação (microsserviços)
Mais componentes para monitorar e proteger, mais pontos de falha de rede, necessidade de tracing (ex.: OpenTelemetry),
versionamento de contratos, gerenciamento de segredos por serviço e equipe de plataforma.

## Benefício esperado
Escala independente, deploy independente e isolamento de falhas **somente se** houver times e carga que justifiquem.

## Recomendação
Comece com **monólito modular** (limites claros do ex. 38). Extraia um serviço quando houver sinal concreto
(ex.: Pagamento exige escala própria e isolamento de segurança). Começar com microsserviços sem esses sinais costuma custar mais do que entrega.
