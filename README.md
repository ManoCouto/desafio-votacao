# Desafio - Golden Raspberry Awards API

API REST para leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards.

## Tecnologias

- Java 17
- Spring Boot 3.5
- H2 (banco em memória)
- Maven

## Como rodar

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta 8080. O CSV `movielist.csv` é carregado automaticamente ao iniciar.

## Endpoints

| Método | URL | Descrição |
|--------|-----|-----------|
| GET | `/api/movies/awards/intervals` | Retorna produtores com maior e menor intervalo entre prêmios |

### Exemplo de resposta

```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Andrew G. Vajna",
      "interval": 12,
      "previousWin": 1994,
      "followingWin": 2006
    }
  ]
}
```

## Console H2

Com a aplicação rodando, acesse: http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:votacao-db`
- User: `sa`
- Password: (vazio)

## Rodar os testes

```bash
./mvnw test
```
