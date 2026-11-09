# Exercício 46 – Dashboard conceitual de observabilidade

Quatro painéis (inspirados nos "golden signals"):

| Painel | Métrica | Como medir | Alerta sugerido |
|--------|---------|-----------|-----------------|
| **Taxa de erro** | % de respostas 5xx sobre o total | `http_server_requests_seconds_count{status=~"5.."}` / total (Micrometer/Prometheus) | > 2% por 5 min |
| **Latência p95** | tempo em que 95% das requisições terminam | `histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))` | p95 > 800 ms por 10 min |
| **Throughput** | requisições por segundo | `rate(http_server_requests_seconds_count[1m])` | queda de 50% vs. mesma hora da semana anterior |
| **Lag do consumidor** | mensagens pendentes por consumer group (Kafka) ou tamanho da fila (RabbitMQ) | `kafka_consumergroup_lag` / exporter | lag crescendo por 10 min |

Complementos: um painel de **saturação** (CPU, memória, pool de conexões do banco) e logs com `correlationId`
para ligar um erro do painel ao pedido exato. Use **p95/p99** (e não a média) porque a média esconde os casos lentos.
