# Exercício 1 – Autenticação x autorização

- **Autenticação** responde "**quem é você?**": o sistema confere uma credencial (senha, token, certificado).
  Exemplo: o usuário informa e-mail e senha no login e a API confirma que é a Ana.
- **Autorização** responde "**o que você pode fazer?**": depois de saber quem é, o sistema decide o acesso.
  Exemplo: a Ana (papel `USER`) pode consultar pedidos, mas só quem tem `ROLE_ADMIN` pode excluir um produto
  (sem permissão, a API devolve **403 Forbidden**; sem estar autenticado, devolve **401 Unauthorized**).

A autenticação vem primeiro; a autorização depende dela.
