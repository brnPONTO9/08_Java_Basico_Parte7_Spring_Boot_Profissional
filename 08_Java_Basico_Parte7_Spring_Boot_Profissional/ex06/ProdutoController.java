package ex06;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    record ProdutoDto(Long id, String nome) { }

    @Operation(summary = "Busca um produto pelo id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDto> buscar(@PathVariable Long id) {
        return id == 1 ? ResponseEntity.ok(new ProdutoDto(1L, "Mouse")) : ResponseEntity.notFound().build();
    }
}
