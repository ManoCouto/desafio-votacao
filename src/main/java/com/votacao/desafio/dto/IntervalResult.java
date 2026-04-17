package com.votacao.desafio.dto;

import java.util.List;

public record IntervalResult(List<ProducerInterval> min, List<ProducerInterval> max) {
}
