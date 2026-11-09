package ex29;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/*
 * A CHAVE da mensagem decide a partição (hash da chave). Mensagens com a mesma chave caem na mesma partição,
 * e o Kafka garante ordem DENTRO de uma partição. Logo, usando pedidoId como chave, todos os eventos do mesmo
 * pedido (CRIADO, PAGO, ENVIADO) são consumidos na ordem correta. Entre pedidos diferentes não há garantia de ordem
 * (e nem precisa), o que permite paralelismo entre partições.
 */
@Component
public class PedidoKafkaProducer {
    private final KafkaTemplate<String, String> kafka;

    public PedidoKafkaProducer(KafkaTemplate<String, String> kafka) { this.kafka = kafka; }

    public void publicar(long pedidoId, String eventoJson) {
        kafka.send("pedidos.eventos", String.valueOf(pedidoId), eventoJson);
    }
}
