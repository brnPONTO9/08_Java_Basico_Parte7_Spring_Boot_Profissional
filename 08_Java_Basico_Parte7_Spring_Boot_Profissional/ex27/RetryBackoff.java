package ex27;

import java.util.function.Supplier;

/**
 * Exercício 27: retry com backoff exponencial para uma consulta IDEMPOTENTE (ex.: GET de frete).
 * Só se repete consulta que não altera estado. Repetir um POST de pagamento sem idempotency key duplicaria a cobrança.
 */
public class RetryBackoff {

    static <T> T comRetry(Supplier<T> operacao, int maxTentativas, long esperaInicialMs) {
        long espera = esperaInicialMs;
        for (int tentativa = 1; ; tentativa++) {
            try {
                return operacao.get();
            } catch (IllegalStateException e) { // só falhas transitórias
                if (tentativa == maxTentativas) throw new IllegalStateException("Falhou após " + tentativa + " tentativas", e);
                System.out.println("Tentativa " + tentativa + " falhou (" + e.getMessage() + "); aguardando " + espera + " ms");
                try { Thread.sleep(espera); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); throw new IllegalStateException(ie); }
                espera *= 2; // 100, 200, 400...
            }
        }
    }

    static int chamadas = 0;

    static double consultarFrete(String cep) {
        if (++chamadas < 3) throw new IllegalStateException("503 indisponível");
        return 25.90;
    }

    public static void main(String[] args) {
        double frete = comRetry(() -> consultarFrete("95600-000"), 5, 100);
        System.out.println("Frete obtido após " + chamadas + " chamadas: R$ " + frete);
        try { comRetry(() -> { throw new IllegalStateException("sempre cai"); }, 3, 50); }
        catch (IllegalStateException e) { System.out.println("Desistiu: " + e.getMessage()); }
    }
}
