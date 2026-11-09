package ex33;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/** HTTP Interface: cliente declarativo (o Spring gera a implementação). */
interface ClienteClient {
    record ClienteDto(Long id, String nome) { }

    @GetExchange("/clientes/{id}")
    ClienteDto buscar(@org.springframework.web.bind.annotation.PathVariable Long id);
}

@Configuration
class ClienteClientConfig {
    @Bean
    ClienteClient clienteClient(RestClient.Builder builder) {
        RestClient rest = builder.baseUrl("http://servico-clientes").build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(rest)).build().createClient(ClienteClient.class);
    }
}

@Service
class PedidoClienteService { // injeta a interface como qualquer bean
    private final ClienteClient clientes;
    PedidoClienteService(ClienteClient clientes) { this.clientes = clientes; }
    String nomeDoCliente(Long id) { return clientes.buscar(id).nome(); }
}
