package ex45;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Exercício 45: circuit breaker + idempotency key para uma integração de pagamento (simulação).
 * Circuit breaker: após N falhas seguidas ABRE e rejeita rápido (sem chamar o serviço) por um tempo; depois testa (meio-aberto).
 * Idempotency key: repetir a mesma chave devolve o resultado anterior, sem cobrar duas vezes.
 */
public class CircuitBreakerIdempotente {
    enum Estado { FECHADO, ABERTO, MEIO_ABERTO }

    static class CircuitBreaker {
        Estado estado = Estado.FECHADO;
        int falhasSeguidas = 0;
        long abertoEm = 0;
        final int limite; final long esperaMs;
        CircuitBreaker(int limite, long esperaMs) { this.limite = limite; this.esperaMs = esperaMs; }

        <T> T executar(Supplier<T> chamada, long agora) {
            if (estado == Estado.ABERTO) {
                if (agora - abertoEm >= esperaMs) estado = Estado.MEIO_ABERTO;
                else throw new IllegalStateException("circuito ABERTO: chamada rejeitada sem tentar");
            }
            try {
                T r = chamada.get();
                falhasSeguidas = 0; estado = Estado.FECHADO;
                return r;
            } catch (RuntimeException e) {
                if (estado == Estado.MEIO_ABERTO || ++falhasSeguidas >= limite) { estado = Estado.ABERTO; abertoEm = agora; }
                throw e;
            }
        }
    }

    static final Map<String, String> resultadosPorChave = new HashMap<>();
    static int cobrancasReais = 0;
    static boolean gatewayFora = false;

    static String cobrar(String idempotencyKey, double valor, CircuitBreaker cb, long agora) {
        String anterior = resultadosPorChave.get(idempotencyKey);
        if (anterior != null) return anterior + " (reaproveitado, sem nova cobrança)";
        String r = cb.executar(() -> {
            if (gatewayFora) throw new IllegalStateException("gateway indisponível");
            cobrancasReais++;
            return "APROVADO R$ " + valor;
        }, agora);
        resultadosPorChave.put(idempotencyKey, r);
        return r;
    }

    public static void main(String[] args) {
        CircuitBreaker cb = new CircuitBreaker(2, 1000);
        System.out.println(cobrar("k1", 100, cb, 0));
        System.out.println(cobrar("k1", 100, cb, 10)); // retry do cliente com a mesma chave
        System.out.println("Cobranças reais: " + cobrancasReais + " (deve ser 1)");

        gatewayFora = true;
        for (int i = 2; i <= 4; i++) {
            try { cobrar("k" + i, 50, cb, 100 + i); }
            catch (IllegalStateException e) { System.out.println("k" + i + ": " + e.getMessage() + " [estado=" + cb.estado + "]"); }
        }
        gatewayFora = false;
        System.out.println(cobrar("k5", 70, cb, 2000) + " [estado=" + cb.estado + "] (após o tempo de espera, meio-aberto -> fechado)");
    }
}
