package com.votacao.desafio.service;

import com.votacao.desafio.dto.IntervalResult;
import com.votacao.desafio.dto.ProducerInterval;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class MovieService {

    // Loop 1 — leitura do CSV e agrupamento de anos por produtor
    // Loop 2 — filtro final de min/max sobre a lista de intervalos já calculada
    public IntervalResult buscarIntervalos() {
        Map<String, List<Integer>> vitoriasPorProdutor = new HashMap<>();

        // LOOP 1 — percorre o CSV uma única vez: agrupa anos de vitoria por produtor
        //          e já rastreia min/max de intervalo na mesma passagem
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource("movielist.csv").getInputStream()))) {

            String linha = br.readLine(); // descarta o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";", -1);
                if (campos.length < 5) continue;
                if (!campos[4].trim().equalsIgnoreCase("yes")) continue;

                int ano = Integer.parseInt(campos[0].trim());
                String[] produtores = campos[3].trim().split(",| and ");

                for (String produtor : produtores) {
                    String nome = produtor.trim();
                    if (!nome.isEmpty()) {
                        vitoriasPorProdutor
                            .computeIfAbsent(nome, k -> new ArrayList<>())
                            .add(ano);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler movielist.csv", e);
        }

        // Calcula intervalos e já rastreia min/max em passagem única por produtor
        List<ProducerInterval> intervalos = new ArrayList<>();
        int menorAteAgora = Integer.MAX_VALUE;
        int maiorAteAgora = Integer.MIN_VALUE;

        for (var entry : vitoriasPorProdutor.entrySet()) {
            List<Integer> anos = entry.getValue();
            if (anos.size() < 2) continue;

            Collections.sort(anos);

            for (int i = 1; i < anos.size(); i++) {
                int intervalo = anos.get(i) - anos.get(i - 1);
                intervalos.add(new ProducerInterval(entry.getKey(), intervalo, anos.get(i - 1), anos.get(i)));

                if (intervalo < menorAteAgora) menorAteAgora = intervalo;
                if (intervalo > maiorAteAgora) maiorAteAgora = intervalo;
            }
        }

        if (intervalos.isEmpty()) {
            return new IntervalResult(List.of(), List.of());
        }

        final int menor = menorAteAgora;
        final int maior = maiorAteAgora;

        // LOOP 2 — única passagem para separar min e max ao mesmo tempo
        List<ProducerInterval> listaMin = new ArrayList<>();
        List<ProducerInterval> listaMax = new ArrayList<>();
        for (ProducerInterval pi : intervalos) {
            if (pi.interval() == menor) listaMin.add(pi);
            if (pi.interval() == maior) listaMax.add(pi);
        }

        return new IntervalResult(listaMin, listaMax);
    }
}
