package ex03;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class SenhaTeste {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(); // em Spring: declare como @Bean

    @Test
    void encodeEMatches() {
        String hash = encoder.encode("minhaSenha123");
        assertNotEquals("minhaSenha123", hash);                     // nunca guarda texto puro
        assertTrue(encoder.matches("minhaSenha123", hash));         // confere a senha digitada contra o hash
        assertFalse(encoder.matches("outraSenha", hash));
        assertNotEquals(hash, encoder.encode("minhaSenha123"));     // salt aleatório: hashes diferentes
    }
}
