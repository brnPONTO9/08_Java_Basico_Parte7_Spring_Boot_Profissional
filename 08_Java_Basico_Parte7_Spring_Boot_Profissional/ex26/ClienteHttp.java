package ex26;

import java.io.IOException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClienteHttp {
    static class RecursoNaoEncontradoException extends RuntimeException {
        RecursoNaoEncontradoException(String m) { super(m); }
    }
    static class ErroDeNegocioException extends RuntimeException {
        ErroDeNegocioException(String m) { super(m); }
    }
    static class ServicoIndisponivelException extends RuntimeException {
        ServicoIndisponivelException(String m) { super(m); }
    }

    private final RestClient restClient;

    public ClienteHttp(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://servico-externo").build();
    }

    public String buscar(String caminho) {
        return restClient.get().uri(caminho).retrieve()
                // 4xx: erro do cliente -> exceção de domínio, NÃO adianta tentar de novo
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    if (res.getStatusCode().value() == 404) throw new RecursoNaoEncontradoException("Recurso não existe: " + caminho);
                    throw new ErroDeNegocioException("Requisição rejeitada: " + res.getStatusCode());
                })
                // 5xx: falha do servidor -> indisponibilidade, pode valer retry/circuit breaker
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new ServicoIndisponivelException("Serviço externo falhou: " + res.getStatusCode());
                })
                .body(String.class);
    }
}
