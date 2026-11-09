package ex28;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    static final String EXCHANGE = "pedidos.exchange";
    static final String FILA = "pedidos.criados";
    static final String FILA_DLQ = "pedidos.criados.dlq";
    static final String ROTA = "pedido.criado";

    @Bean DirectExchange exchange() { return new DirectExchange(EXCHANGE); }

    @Bean
    Queue filaPrincipal() { // mensagens rejeitadas/esgotadas vão para a DLQ via dead-letter exchange
        return QueueBuilder.durable(FILA)
                .withArgument("x-dead-letter-exchange", "")             // exchange padrão
                .withArgument("x-dead-letter-routing-key", FILA_DLQ)   // roteia direto para a fila DLQ
                .build();
    }

    @Bean Queue filaDlq() { return QueueBuilder.durable(FILA_DLQ).build(); }

    @Bean
    Binding binding(Queue filaPrincipal, DirectExchange exchange) {
        return BindingBuilder.bind(filaPrincipal).to(exchange).with(ROTA);
    }
}
// No consumidor: spring.rabbitmq.listener.simple.default-requeue-rejected=false  (rejeitar = ir para a DLQ, não voltar à fila)
