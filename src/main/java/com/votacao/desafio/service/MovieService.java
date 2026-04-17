package com.votacao.desafio.service;

import com.votacao.desafio.dto.IntervalResult;
import com.votacao.desafio.dto.ProducerInterval;
import com.votacao.desafio.model.Movie;
import com.votacao.desafio.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    public void carregarCsv() {
        try (var br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/movielist.csv")))) {

            String linha = br.readLine(); // pula o header
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";", -1);
                if (campos.length < 4) continue;

                Movie filme = new Movie();
                filme.setYear(Integer.parseInt(campos[0].trim()));
                filme.setTitle(campos[1].trim());
                filme.setStudios(campos[2].trim());
                filme.setProducers(campos[3].trim());
                filme.setWinner(campos.length > 4 && campos[4].trim().equalsIgnoreCase("yes"));

                movieRepository.save(filme);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar movielist.csv", e);
        }
    }

    public IntervalResult buscarIntervalos() {
        List<Movie> vencedores = movieRepository.findByWinnerTrue();

        // agrupa os anos que cada produtor ganhou
        Map<String, List<Integer>> vitoriasPorProdutor = new HashMap<>();

        for (Movie filme : vencedores) {
            String[] produtores = filme.getProducers().split(",| and ");
            for (String produtor : produtores) {
                String nome = produtor.trim();
                if (!nome.isEmpty()) {
                    vitoriasPorProdutor
                        .computeIfAbsent(nome, k -> new ArrayList<>())
                        .add(filme.getYear());
                }
            }
        }

        // calcula os intervalos entre vitorias consecutivas
        List<ProducerInterval> intervalos = new ArrayList<>();

        for (var entry : vitoriasPorProdutor.entrySet()) {
            List<Integer> anos = entry.getValue();
            Collections.sort(anos);

            for (int i = 1; i < anos.size(); i++) {
                intervalos.add(new ProducerInterval(
                    entry.getKey(),
                    anos.get(i) - anos.get(i - 1),
                    anos.get(i - 1),
                    anos.get(i)
                ));
            }
        }

        if (intervalos.isEmpty()) {
            return new IntervalResult(List.of(), List.of());
        }

        int menor = intervalos.stream()
            .mapToInt(ProducerInterval::interval).min().orElse(0);
        int maior = intervalos.stream()
            .mapToInt(ProducerInterval::interval).max().orElse(0);

        List<ProducerInterval> listaMin = intervalos.stream()
            .filter(i -> i.interval() == menor).toList();
        List<ProducerInterval> listaMax = intervalos.stream()
            .filter(i -> i.interval() == maior).toList();

        return new IntervalResult(listaMin, listaMax);
    }
}
