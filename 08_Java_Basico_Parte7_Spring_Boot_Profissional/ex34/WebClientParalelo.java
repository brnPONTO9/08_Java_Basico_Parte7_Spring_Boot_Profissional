package ex34;

import java.time.Duration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientParalelo {
    record Cliente(Long id, String nome) { }
    record Frete(String cep, double valor) { }
    record Resumo(Cliente cliente, Frete frete) { }

    private final WebClient web = WebClient.builder().baseUrl("http://servicos-internos").build();

    public Resumo montar(Long clienteId, String cep) {
        Mono<Cliente> cliente = web.get().uri("/clientes/{id}", clienteId).retrieve().bodyToMono(Cliente.class);
        Mono<Frete> frete = web.get().uri("/fretes/{cep}", cep).retrieve().bodyToMono(Frete.class);
        return Mono.zip(cliente, frete)                 // as DUAS chamadas rodam em paralelo
                .map(t -> new Resumo(t.getT1(), t.getT2()))
                .timeout(Duration.ofSeconds(2))         // limite total de espera
                .block();                               // em app MVC; em WebFlux devolva o Mono
    }
}
