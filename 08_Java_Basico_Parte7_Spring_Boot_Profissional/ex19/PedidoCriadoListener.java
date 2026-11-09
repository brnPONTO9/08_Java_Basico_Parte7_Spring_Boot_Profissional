package ex19;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

record PedidoCriadoEvent(Long pedidoId, java.math.BigDecimal total) { }

@Component
public class PedidoCriadoListener {
    private static final Logger log = LoggerFactory.getLogger(PedidoCriadoListener.class);

    // Requer um MessageConverter JSON (ex.: bean Jackson2JsonMessageConverter) para converter o corpo em PedidoCriadoEvent
    @RabbitListener(queues = "pedidos.criados")
    public void receber(PedidoCriadoEvent evento) {
        log.info("Pedido criado recebido: id={} total={}", evento.pedidoId(), evento.total());
    }
}
