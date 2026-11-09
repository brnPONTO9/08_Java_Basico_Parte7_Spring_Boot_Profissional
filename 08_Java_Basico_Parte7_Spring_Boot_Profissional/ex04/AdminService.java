package ex04;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

// Habilite uma vez na configuração:  @EnableMethodSecurity  (em uma classe @Configuration)
@Service
public class AdminService {

    @PreAuthorize("hasRole('ADMIN')") // exige a authority ROLE_ADMIN; senão lança AccessDeniedException (403)
    public void excluirUsuario(Long id) {
        // ... regra de negócio
    }
}
