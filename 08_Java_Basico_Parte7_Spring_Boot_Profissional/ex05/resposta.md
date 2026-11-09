# Exercício 5 – As três partes de um JWT

Um JWT tem a forma `xxxxx.yyyyy.zzzzz` (cada parte em Base64URL):

1. **Header** – metadados: algoritmo de assinatura (`alg`, ex. HS256/RS256) e tipo (`typ: JWT`).
2. **Payload** – as *claims*: `sub` (usuário), `iss` (emissor), `aud` (destinatário), `exp` (expiração), `scope`/roles etc.
3. **Signature** – assinatura calculada com o header + payload e uma chave. Garante **integridade e origem**:
   se alguém alterar o payload, a assinatura deixa de bater.

**Por que o payload não deve conter segredo:** o payload é apenas **codificado** (Base64URL), **não criptografado**.
Qualquer pessoa que tenha o token consegue decodificar e ler o conteúdo. A assinatura impede *alterar*, não impede *ler*.
Por isso nunca coloque senha, número de cartão ou dados sensíveis nas claims.
