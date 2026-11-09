package ex37;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercício 37: cache stampede = muitas requisições chegam ao mesmo tempo quando uma chave EXPIRA e todas vão ao banco.
 * Simulamos 200 chaves criadas ao mesmo tempo: sem jitter, todas expiram no mesmo instante;
 * com jitter (TTL +/- aleatório), as expirações se espalham no tempo.
 */
public class CacheStampedeJitter {
    record Entrada(String valor, long expiraEm) { }

    final Map<String, Entrada> cache = new ConcurrentHashMap<>();
    final AtomicInteger idasAoBanco = new AtomicInteger();

    String buscar(String chave, long agora, long ttl, boolean comJitter) {
        Entrada e = cache.get(chave);
        if (e != null && e.expiraEm() > agora) return e.valor();
        idasAoBanco.incrementAndGet(); // miss: vai ao banco
        long ttlFinal = comJitter ? ttl + ThreadLocalRandom.current().nextLong(-ttl / 5, ttl / 5 + 1) : ttl; // +/-20%
        cache.put(chave, new Entrada("v-" + chave, agora + ttlFinal));
        return "v-" + chave;
    }

    static int maiorPicoPorTick(boolean comJitter) {
        CacheStampedeJitter c = new CacheStampedeJitter();
        long ttl = 100; // 100 "ticks" de tempo
        for (int k = 0; k < 200; k++) c.buscar("k" + k, 0, ttl, comJitter); // aquecimento no tick 0
        c.idasAoBanco.set(0);
        int pico = 0;
        for (long t = 1; t <= 150; t++) {
            c.idasAoBanco.set(0);
            for (int k = 0; k < 200; k++) c.buscar("k" + k, t, ttl, comJitter);
            pico = Math.max(pico, c.idasAoBanco.get());
        }
        return pico;
    }

    public static void main(String[] args) {
        System.out.println("Pico de idas ao banco em um único tick SEM jitter: " + maiorPicoPorTick(false));
        System.out.println("Pico de idas ao banco em um único tick COM jitter: " + maiorPicoPorTick(true));
    }
}
