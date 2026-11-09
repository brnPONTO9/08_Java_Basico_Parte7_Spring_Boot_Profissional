package ex32;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SegurancaMvcTest {
    @Autowired MockMvc mvc;

    @Test
    void semTokenRetorna401() throws Exception {
        mvc.perform(get("/api/pedidos")).andExpect(status().isUnauthorized());
    }

    @Test
    void semScopeRetorna403() throws Exception {
        mvc.perform(get("/api/pedidos").with(jwt())) // token válido, porém SEM o scope pedidos:read
                .andExpect(status().isForbidden());
    }

    @Test
    void comScopeRetorna200() throws Exception {
        mvc.perform(get("/api/pedidos").with(jwt().authorities(() -> "SCOPE_pedidos:read")))
                .andExpect(status().isOk());
    }
}
