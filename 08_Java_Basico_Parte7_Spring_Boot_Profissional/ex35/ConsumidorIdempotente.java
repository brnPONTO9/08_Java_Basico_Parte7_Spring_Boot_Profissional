package ex35;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Exercício 35: consumidor idempotente. Brokers entregam "pelo menos uma vez", então a mesma mensagem pode chegar
 * duas vezes. Guardamos o id dos eventos já processados e ignoramos repetições.
 * (No sistema real: tabela com UNIQUE(evento_id) gravada na MESMA transação do efeito.)
 */
public class ConsumidorIdempotente {
    record Evento(String id, String pedido, double valor) { }

    private final Set<String> processados = new HashSet<>();
    private double faturamento = 0;

    void receber(Evento e) {
        if (!processados.add(e.id())) { // add devolve false se já existia
            System.out.println("Ignorado (duplicado): " + e.id());
            return;
        }
        faturamento += e.valor();
        System.out.println("Processado: " + e.id() + " (" + e.pedido() + ")");
    }

    public static void main(String[] args) {
        ConsumidorIdempotente c = new ConsumidorIdempotente();
        List<Evento> chegada = List.of(
                new Evento("ev-1", "P1", 100), new Evento("ev-2", "P2", 50),
                new Evento("ev-1", "P1", 100), // reentrega
                new Evento("ev-3", "P3", 25), new Evento("ev-2", "P2", 50));
        chegada.forEach(c::receber);
        System.out.println("Faturamento: " + c.faturamento + " (esperado 175)");
    }
}
