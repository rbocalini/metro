package com.statusmetro.service;

import com.statusmetro.config.ExternalApiException;
import com.statusmetro.dto.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LineStatusService {

    private static final Set<Integer> METRO_LINES = Set.of(1, 2, 3, 4, 5, 15);

    private final RestClient restClient;

    public LineStatusService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Cacheable("lineStatus")
    @SuppressWarnings("unchecked")
    public LineStatusResponse getLineStatus() {
        Map<String, Object> body;
        try {
            body = restClient.get()
                    .uri("/v1/mobility/public/line-status/current/state/SP")
                    .retrieve()
                    .body(Map.class);
        } catch (Exception ex) {
            throw new ExternalApiException(
                    "Unable to fetch line status from data source. Please try again later.", ex);
        }

        if (body == null || body.get("data") == null) {
            throw new ExternalApiException("External API returned empty response.");
        }

        Map<String, Object> data = (Map<String, Object>) body.get("data");
        String lastUpdated = (String) data.get("dataAtualizacao");
        List<Map<String, Object>> concessoes =
                (List<Map<String, Object>>) data.get("concessoes");

        List<LineDTO> allLines = new ArrayList<>();
        for (Map<String, Object> concessao : concessoes) {
            String operatorName = (String) concessao.get("nome");
            List<Map<String, Object>> linhas =
                    (List<Map<String, Object>>) concessao.get("linhas");

            for (Map<String, Object> linha : linhas) {
                allLines.add(mapLine(linha, operatorName));
            }
        }

        List<LineDTO> metroLines = allLines.stream()
                .filter(l -> METRO_LINES.contains(l.number()))
                .sorted(Comparator.comparingInt(LineDTO::number))
                .toList();

        List<LineDTO> trensLines = allLines.stream()
                .filter(l -> !METRO_LINES.contains(l.number()))
                .sorted(Comparator.comparingInt(LineDTO::number))
                .toList();

        return new LineStatusResponse(lastUpdated, List.of(
                new LineGroupDTO("Metrô", metroLines),
                new LineGroupDTO("Trens", trensLines)
        ));
    }

    @SuppressWarnings("unchecked")
    private LineDTO mapLine(Map<String, Object> linha, String operator) {
        String uid = (String) linha.get("uid");
        Object numeroRaw = linha.get("numero");
        int number = (numeroRaw instanceof Number n) ? n.intValue()
                : Integer.parseInt(numeroRaw.toString());
        String name = (String) linha.get("nome");
        String colorHex = (String) linha.get("corRgb");

        Map<String, Object> statusMap =
                (Map<String, Object>) linha.get("statusLinha");
        String statusCode = (String) statusMap.get("codigo");
        String statusLabel = (String) statusMap.get("status");
        String description = (String) statusMap.get("descricao");

        String category = METRO_LINES.contains(number) ? "metro" : "trens";

        return new LineDTO(uid, number, name, operator, colorHex,
                new LineStatusDTO(statusCode, statusLabel, description),
                category);
    }
}
