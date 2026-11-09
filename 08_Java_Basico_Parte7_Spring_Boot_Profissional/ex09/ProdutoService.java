package ex09;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

// Habilite o cache uma vez:  @EnableCaching  em uma classe @Configuration
@Service
public class ProdutoService {

    @Cacheable("produtos") // a chave padrão é o parâmetro id; a 2ª chamada com o mesmo id nem executa o método
    public String buscarProduto(Long id) {
        System.out.println("Buscando no banco o produto " + id);
        return "Produto " + id;
    }
}
