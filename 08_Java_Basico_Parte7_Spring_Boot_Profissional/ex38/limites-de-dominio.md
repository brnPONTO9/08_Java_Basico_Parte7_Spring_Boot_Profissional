# Exercício 38 – Limites de domínio (sem criar microsserviços automaticamente)

Desenhamos **módulos** dentro de um único deployable (monólito modular). Cada módulo é dono dos seus dados.

| Módulo | Responsabilidade | Dados que possui | Expõe |
|--------|------------------|------------------|-------|
| **Catálogo** | Produtos, preços, descrição | `produto`, `categoria` | consultar produto/preço |
| **Estoque** | Quantidade disponível, reservas | `estoque`, `reserva` | reservar/liberar quantidade |
| **Pedido** | Ciclo de vida do pedido (criado, pago, enviado, cancelado) | `pedido`, `item_pedido` | criar/consultar pedido; evento `PedidoCriado` |
| **Pagamento** | Cobrança, estorno, status do pagamento | `pagamento` | cobrar/estornar; evento `PagamentoAprovado` |

## Regras de fronteira
1. Um módulo **nunca** lê as tabelas de outro; só usa a interface pública (ou eventos).
2. Referência entre módulos é por **id** (`pedido.produto_id`), sem JOIN entre donos diferentes.
3. Fluxo: Pedido cria `PedidoCriado` → Estoque reserva → Pagamento cobra → Pedido muda de estado.
4. Dentro do monólito a comunicação pode ser chamada de método/evento em memória; as interfaces já ficam prontas
   para virar rede (fila/HTTP) **se e quando** houver motivo real (escala independente, times, ciclo de release diferente).

## Quando extrair um módulo para microsserviço?
Somente com sinal claro: carga muito diferente (ex.: Pagamento precisa escalar sozinho), equipes autônomas,
requisitos de isolamento/segurança (PCI). Caso contrário o custo (rede, observabilidade, consistência eventual) não compensa.
