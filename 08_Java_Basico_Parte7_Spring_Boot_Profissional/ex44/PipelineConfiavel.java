package ex44;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Exercício 44: Outbox + consumidor idempotente + DLQ para falhas persistentes (simulação).
 * - Outbox: eventos nascem junto com o pedido.
 * - Consumidor idempotente: ignora evento já processado.
 * - Retentativas limitadas: após N falhas a mensagem vai para a DLQ para análise humana.
 */
public class PipelineConfiavel {
    record Evento(String id, String pedido) { }

    static final int MAX_TENTATIVAS = 3;
    final Queue<Evento> outbox = new ArrayDeque<>();
    final Queue<Evento> dlq = new ArrayDeque<>();
    final Set<String> processados = new HashSet<>();
    final Map<String, Integer> tentativas = new HashMap<>();
    final List<String> efeitos = new ArrayList<>();

    boolean processar(Evento e) {
        if (e.pedido().equals("VENENO")) throw new IllegalStateException("falha persistente"); // sempre falha
        return efeitos.add("faturado " + e.pedido());
    }

    void consumirTudo() {
        Queue<Evento> fila = new ArrayDeque<>(outbox);
        outbox.clear();
        while (!fila.isEmpty()) {
            Evento e = fila.poll();
            if (processados.contains(e.id())) { System.out.println("Duplicado ignorado: " + e.id()); continue; }
            try {
                processar(e);
                processados.add(e.id());
            } catch (IllegalStateException ex) {
                int n = tentativas.merge(e.id(), 1, Integer::sum);
                if (n >= MAX_TENTATIVAS) { dlq.add(e); System.out.println("-> DLQ: " + e.id() + " após " + n + " tentativas"); }
                else { System.out.println("Falha " + n + " em " + e.id() + "; reenfileirando"); fila.add(e); }
            }
        }
    }

    public static void main(String[] args) {
        PipelineConfiavel p = new PipelineConfiavel();
        p.outbox.add(new Evento("e1", "P1"));
        p.outbox.add(new Evento("e2", "VENENO"));
        p.outbox.add(new Evento("e1", "P1")); // duplicado
        p.outbox.add(new Evento("e3", "P3"));
        p.consumirTudo();
        System.out.println("Efeitos: " + p.efeitos + " | DLQ: " + p.dlq.size());
    }
}
