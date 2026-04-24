# Desafio - Golden Raspberry Awards API

API REST para leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards.

## Tecnologias

- Java 17
- Spring Boot 3.5
- Maven

## Como rodar

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta 8080. O CSV `movielist.csv` é lido diretamente ao receber a requisição.

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

## Rodar os testes

```bash
./mvnw test
```
