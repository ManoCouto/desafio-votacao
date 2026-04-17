package com.votacao.desafio.dto;

public record ProducerInterval(String producer, int interval, int previousWin, int followingWin) {
}
