package com.votacao.desafio.controller;

import com.votacao.desafio.dto.IntervalResult;
import com.votacao.desafio.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/awards/intervals")
    public ResponseEntity<IntervalResult> buscarIntervalos() {
        return ResponseEntity.ok(movieService.buscarIntervalos());
    }
}
