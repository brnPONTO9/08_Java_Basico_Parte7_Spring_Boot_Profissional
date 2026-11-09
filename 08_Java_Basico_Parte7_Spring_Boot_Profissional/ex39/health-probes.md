# Exercício 39 – Health, liveness e readiness

```yaml
management:
  endpoint:
    health:
      probes:
        enabled: true
  endpoints:
    web:
      exposure:
        include: health,info
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
```
Endpoints: `/actuator/health/liveness` e `/actuator/health/readiness`.

| Probe | Pergunta | O que entra | O que NÃO entra |
|-------|----------|-------------|-----------------|
| **Liveness** | "O processo está vivo ou travado?" Se falhar, a plataforma **reinicia** o contêiner. | Estado interno da aplicação (ex.: deadlock, `LivenessState`). | Banco, Redis, broker: se eles caírem, reiniciar a API não resolve e ainda piora (reinício em cascata). |
| **Readiness** | "Posso receber tráfego agora?" Se falhar, o balanceador **tira** a instância da rota (sem reiniciar). | Dependências essenciais para atender requisições: **banco de dados** (`db`), cache obrigatório, migração concluída, warm-up. | Dependências opcionais (ex.: serviço de e-mail), para não derrubar a API inteira por um recurso secundário. |

No Kubernetes: `livenessProbe` -> `/actuator/health/liveness`, `readinessProbe` -> `/actuator/health/readiness`.
Habilite também o *graceful shutdown* (`server.shutdown=graceful`) para terminar requisições em andamento antes de parar.
