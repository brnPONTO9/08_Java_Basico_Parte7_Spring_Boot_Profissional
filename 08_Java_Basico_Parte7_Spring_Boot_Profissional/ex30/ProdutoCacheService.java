package ex30;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoCacheService {

    @Cacheable(value = "produtos", key = "#id")
    public String buscar(Long id) { return "produto-" + id; }

    @CacheEvict(value = "produtos", key = "#id") // remove do cache DEPOIS de atualizar, evitando dado velho
    public void atualizar(Long id, String novoNome) {
        // ... UPDATE no banco
    }
}
