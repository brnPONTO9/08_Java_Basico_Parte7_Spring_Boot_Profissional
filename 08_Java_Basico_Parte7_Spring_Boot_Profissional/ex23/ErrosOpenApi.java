package ex23;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class ErrosOpenApi {

    /** Modelo padrão de erro da API (aparece no Swagger). */
    @Schema(description = "Resposta de erro padronizada")
    public record ErroResponse(int status, String mensagem, java.util.List<String> detalhes) { }

    @Operation(summary = "Cria um pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Criado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente ou inválido", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "403", description = "Sem permissão (scope)", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "409", description = "Conflito (estoque insuficiente ou pedido duplicado)", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PostMapping
    public void criar() { }
}
