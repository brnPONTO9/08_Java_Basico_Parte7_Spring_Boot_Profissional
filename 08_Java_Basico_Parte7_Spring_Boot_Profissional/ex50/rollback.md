# Plano de rollback

1. **Gatilho:** taxa de erro > 5% por 5 min, p95 > 2 s, ou readiness falhando após o deploy.
2. **Aplicação:** reimplantar a imagem anterior (tag = SHA do commit anterior, já no registry). Deploy rolling/blue-green permite voltar em minutos.
3. **Banco:** as migrações seguem expand/contract (ex. 47), então a versão anterior continua compatível com o schema novo.
   Nunca executar `DROP`/`contract` no mesmo deploy. Migração destrutiva só depois de a versão nova estar estável.
4. **Mensageria:** consumidores são idempotentes (ex. 35/44); mensagens reprocessadas não duplicam efeito. Conferir a DLQ.
5. **Cache:** limpar chaves afetadas se o formato mudou (versionar a chave: `produto:v2:{id}`).
6. **Comunicação:** avisar o time e registrar um post-mortem curto (causa, impacto, ação corretiva).
