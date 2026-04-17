package com.votacao.desafio;

import com.votacao.desafio.dto.IntervalResult;
import com.votacao.desafio.dto.ProducerInterval;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveRetornarIntervaloMinEMax() {
        ResponseEntity<IntervalResult> resposta = restTemplate
            .getForEntity("/api/movies/awards/intervals", IntervalResult.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());

        IntervalResult resultado = resposta.getBody();
        assertFalse(resultado.min().isEmpty(), "Lista min nao pode ser vazia");
        assertFalse(resultado.max().isEmpty(), "Lista max nao pode ser vazia");

        // Joel Silver ganhou em 1990 e 1991, entao o menor intervalo eh 1
        ProducerInterval menorIntervalo = resultado.min().get(0);
        assertEquals(1, menorIntervalo.interval());
        assertEquals("Joel Silver", menorIntervalo.producer());
        assertEquals(1990, menorIntervalo.previousWin());
        assertEquals(1991, menorIntervalo.followingWin());

        // Andrew G. Vajna ganhou em 1994 e 2006, maior intervalo = 12
        ProducerInterval maiorIntervalo = resultado.max().get(0);
        assertEquals(12, maiorIntervalo.interval());
        assertEquals("Andrew G. Vajna", maiorIntervalo.producer());
        assertEquals(1994, maiorIntervalo.previousWin());
        assertEquals(2006, maiorIntervalo.followingWin());
    }

    @Test
    void intervaloMinDeveSerMenorOuIgualAoMax() {
        ResponseEntity<IntervalResult> resposta = restTemplate
            .getForEntity("/api/movies/awards/intervals", IntervalResult.class);

        IntervalResult resultado = resposta.getBody();
        assertNotNull(resultado);

        int min = resultado.min().get(0).interval();
        int max = resultado.max().get(0).interval();

        assertTrue(min <= max,
            "Intervalo minimo deve ser menor ou igual ao maximo");
    }
}
