package ex18;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FreteClient {
    record FreteResposta(String cep, double valor) { }

    private final RestClient restClient;

    public FreteClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://servico-frete").build();
    }

    public FreteResposta consultar(String cep) {
        return restClient.get()
                .uri("/fretes/{cep}", cep)
                .retrieve()
                .body(FreteResposta.class);
    }
}
