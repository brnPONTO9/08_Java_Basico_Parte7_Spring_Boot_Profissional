package ex22;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Exercício 22: refresh token com ROTAÇÃO e REVOGAÇÃO (simulação em memória).
 * - Cada refresh token só pode ser usado UMA vez: ao usar, ele é invalidado e um novo é emitido.
 * - Se um token já usado aparecer de novo (indício de roubo), toda a "família" é revogada.
 * - No sistema real, guarde apenas o HASH do token no banco (nunca o token puro).
 */
public class RefreshTokenRotacao {
    record Sessao(String usuario, String familia) { }

    private final Map<String, Sessao> ativos = new HashMap<>();
    private final Map<String, String> usados = new HashMap<>(); // token já usado -> família
    private final Set<String> familiasRevogadas = new HashSet<>();
    private final SecureRandom random = new SecureRandom();
    private int seqFamilia = 0;

    private String novoToken() {
        byte[] b = new byte[24];
        random.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    String login(String usuario) {
        String t = novoToken();
        ativos.put(t, new Sessao(usuario, "F" + (++seqFamilia)));
        return t;
    }

    String renovar(String refresh) {
        if (usados.containsKey(refresh)) { // reuso detectado: alguém guardou o token antigo
            revogarFamilia(usados.get(refresh));
            throw new SecurityException("Refresh token já utilizado: possível roubo, família revogada");
        }
        Sessao s = ativos.remove(refresh);
        if (s == null) throw new SecurityException("Refresh token inválido ou revogado");
        if (familiasRevogadas.contains(s.familia())) throw new SecurityException("Família revogada");
        usados.put(refresh, s.familia());
        String novo = novoToken();
        ativos.put(novo, s); // mesma família, token novo
        return novo;
    }

    void revogarFamilia(String familia) {
        familiasRevogadas.add(familia);
        ativos.values().removeIf(s -> s.familia().equals(familia));
    }

    public static void main(String[] args) {
        RefreshTokenRotacao srv = new RefreshTokenRotacao();
        String r1 = srv.login("ana");
        String r2 = srv.renovar(r1);
        System.out.println("Renovou 1x: token novo emitido = " + !r1.equals(r2));
        String r3 = srv.renovar(r2);
        System.out.println("Renovou com o token vigente: " + (r3 != null));
        try { srv.renovar(r1); } catch (SecurityException e) { System.out.println("Reuso do token antigo -> " + e.getMessage()); }
        try { srv.renovar(r3); } catch (SecurityException e) { System.out.println("Token vigente da família revogada -> " + e.getMessage()); }
        String outro = srv.login("bruno");
        System.out.println("Outra sessão (outra família) continua funcionando: " + (srv.renovar(outro) != null));
    }
}
