package ex36;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercício 36: Outbox Pattern. O pedido e o evento são gravados na MESMA transação (tabelas pedido e outbox).
 * Um publicador separado lê a outbox e envia ao broker; se cair, reenvia depois (entrega "pelo menos uma vez").
 * Isso evita o problema do "dual write": gravar no banco e publicar no broker são duas operações que podem falhar separadas.
 * Aqui a "transação" é simulada: ou grava as duas listas, ou nenhuma.
 */
public class OutboxSimulacao {
    record Pedido(long id, double total) { }
    static class EventoOutbox {
        final long id; final String tipo; final String payload; boolean publicado;
        EventoOutbox(long id, String tipo, String payload) { this.id = id; this.tipo = tipo; this.payload = payload; }
    }

    final List<Pedido> pedidos = new ArrayList<>();
    final List<EventoOutbox> outbox = new ArrayList<>();
    long seq = 0;

    void criarPedido(long id, double total, boolean falharNoMeio) {
        List<Pedido> snapPedidos = new ArrayList<>(pedidos);
        List<EventoOutbox> snapOutbox = new ArrayList<>(outbox);
        try { // ---- início da transação ----
            pedidos.add(new Pedido(id, total));
            if (falharNoMeio) throw new IllegalStateException("erro depois de gravar o pedido");
            outbox.add(new EventoOutbox(++seq, "PedidoCriado", "{\"pedidoId\":" + id + "}"));
        } catch (RuntimeException e) { // ---- rollback ----
            pedidos.clear(); pedidos.addAll(snapPedidos);
            outbox.clear(); outbox.addAll(snapOutbox);
            System.out.println("Rollback do pedido " + id + ": " + e.getMessage());
        }
    }

    void publicarPendentes(boolean brokerFora) {
        for (EventoOutbox e : outbox) {
            if (e.publicado) continue;
            if (brokerFora) { System.out.println("Broker indisponível; evento " + e.id + " continua pendente"); continue; }
            System.out.println("Publicado no broker: " + e.tipo + " " + e.payload);
            e.publicado = true;
        }
    }

    public static void main(String[] args) {
        OutboxSimulacao s = new OutboxSimulacao();
        s.criarPedido(1, 100, false);
        s.criarPedido(2, 200, true);   // falha: nem pedido nem evento ficam gravados
        s.publicarPendentes(true);     // broker fora do ar: evento fica na outbox
        s.publicarPendentes(false);    // broker voltou: evento é entregue
        System.out.println("Pedidos gravados: " + s.pedidos.size() + " | eventos na outbox: " + s.outbox.size());
    }
}
