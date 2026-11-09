# Exercício 47 – Migração compatível com rolling deploy (expand/contract)

No *rolling deploy* convivem **duas versões da aplicação** (v1 e v2) usando o **mesmo banco**.
Logo, cada migração precisa funcionar com as duas. Exemplo: renomear `cliente.nome` para `cliente.nome_completo`.

| Fase | Migração / Deploy | v1 funciona? | v2 funciona? |
|------|-------------------|--------------|--------------|
| **1. Expand** | `V2__add_nome_completo.sql`: `ALTER TABLE cliente ADD COLUMN nome_completo VARCHAR(150) NULL;` | sim | – |
| **2. Backfill** | `UPDATE cliente SET nome_completo = nome WHERE nome_completo IS NULL;` (em lotes) | sim | – |
| **3. Deploy v2** | v2 **escreve nas duas colunas** e **lê** `nome_completo` (com fallback para `nome`) | sim | sim |
| **4. Migrar** | Verificar que toda a frota roda v2 e que `nome_completo` não tem nulos | – | sim |
| **5. Contract** | `V3__remove_nome.sql`: `ALTER TABLE cliente DROP COLUMN nome;` em um deploy **posterior** | (v1 já não existe) | sim |

Regras: nunca remover/renomear coluna no mesmo deploy que deixa de usá-la; novas colunas começam `NULL` ou com `DEFAULT`;
cada passo é reversível até a fase *contract*; use Flyway/Liquibase com migrações versionadas e imutáveis.
