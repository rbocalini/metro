package com.statusmetro.service;

import com.statusmetro.dto.LineDTO;
import com.statusmetro.dto.LineGroupDTO;
import com.statusmetro.dto.LineStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LineStatusServiceTest {

    private RestClient restClient;
    private LineStatusService service;

    @BeforeEach
    void setUp() {
        restClient = mock(RestClient.class);
        service = new LineStatusService(restClient);
    }

    @Test
    void shouldReturnTwoGroups_MetroAndTrens() {
        mockApiResponse(buildSampleApiResponse());

        LineStatusResponse response = service.getLineStatus();

        assertEquals(2, response.groups().size());
        assertEquals("Metrô", response.groups().get(0).name());
        assertEquals("Trens", response.groups().get(1).name());
    }

    @Test
    void shouldGroupMetroLines_1to5and15() {
        mockApiResponse(buildSampleApiResponse());

        LineStatusResponse response = service.getLineStatus();

        LineGroupDTO metro = response.groups().get(0);
        List<Integer> metroNumbers = metro.lines().stream()
                .map(LineDTO::number).toList();
        assertTrue(metroNumbers.contains(1));
        assertTrue(metroNumbers.contains(4));
        assertTrue(metroNumbers.contains(15));
        assertFalse(metroNumbers.contains(7));
        assertFalse(metroNumbers.contains(11));
    }

    @Test
    void shouldGroupTrensLines_7to13() {
        mockApiResponse(buildSampleApiResponse());

        LineStatusResponse response = service.getLineStatus();

        LineGroupDTO trens = response.groups().get(1);
        List<Integer> trensNumbers = trens.lines().stream()
                .map(LineDTO::number).toList();
        assertTrue(trensNumbers.contains(7));
        assertTrue(trensNumbers.contains(11));
        assertTrue(trensNumbers.contains(12));
        assertFalse(trensNumbers.contains(1));
        assertFalse(trensNumbers.contains(15));
    }

    @Test
    void shouldSortLinesWithinGroupByNumber() {
        mockApiResponse(buildSampleApiResponse());

        LineStatusResponse response = service.getLineStatus();

        for (LineGroupDTO group : response.groups()) {
            List<Integer> numbers = group.lines().stream()
                    .map(LineDTO::number).toList();
            for (int i = 1; i < numbers.size(); i++) {
                assertTrue(numbers.get(i) >= numbers.get(i - 1),
                        "Lines should be sorted by number");
            }
        }
    }

    @Test
    void shouldMapDtoFieldsCorrectly() {
        mockApiResponse(buildSampleApiResponse());

        LineStatusResponse response = service.getLineStatus();

        LineDTO line1 = response.groups().get(0).lines().get(0);
        assertEquals("METRO-L1", line1.uid());
        assertEquals(1, line1.number());
        assertEquals("Azul", line1.name());
        assertEquals("Metro SP", line1.operator());
        assertEquals("#171796", line1.colorHex());
        assertEquals("OperacaoNormal", line1.status().code());
        assertEquals("Operação Normal", line1.status().label());
        assertEquals("metro", line1.category());
    }

    @Test
    void shouldMapDisruptionDescription() {
        mockApiResponse(buildSampleApiResponse());

        LineStatusResponse response = service.getLineStatus();

        LineGroupDTO trens = response.groups().get(1);
        LineDTO line12 = trens.lines().stream()
                .filter(l -> l.number() == 12).findFirst().orElseThrow();
        assertEquals("VelocidadeReduzida", line12.status().code());
        assertNotNull(line12.status().description());
        assertTrue(line12.status().description().contains("velocidade reduzida"));
    }

    @Test
    void shouldExtractLastUpdatedTimestamp() {
        mockApiResponse(buildSampleApiResponse());

        LineStatusResponse response = service.getLineStatus();

        assertEquals("2026-04-02T17:00:04", response.lastUpdated());
    }

    @SuppressWarnings("unchecked")
    private void mockApiResponse(Map<String, Object> body) {
        RequestHeadersUriSpec uriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec headersSpec = mock(RequestHeadersSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(body);
    }

    private Map<String, Object> buildSampleApiResponse() {
        return Map.of(
            "status", true,
            "message", "",
            "errorCode", "",
            "data", Map.of(
                "dataAtualizacao", "2026-04-02T17:00:04",
                "concessoes", List.of(
                    Map.of(
                        "uid", "METRO",
                        "nome", "Metro SP",
                        "linhas", List.of(
                            buildLine("METRO-L1", 1, "Azul", "#171796",
                                "OperacaoNormal", "Operação Normal", null),
                            buildLine("METRO-L3", 3, "Vermelha", "#ED2E38",
                                "OperacaoNormal", "Operação Normal", null),
                            buildLine("METRO-L15", 15, "Prata", "#8F8F8C",
                                "OperacaoNormal", "Operação Normal", null)
                        )
                    ),
                    Map.of(
                        "uid", "CCRVQ",
                        "nome", "ViaQuatro",
                        "linhas", List.of(
                            buildLine("CCRVQ-L4", 4, "Amarela", "#FCC540",
                                "OperacaoNormal", "Operação Normal", null)
                        )
                    ),
                    Map.of(
                        "uid", "CPTM",
                        "nome", "CPTM",
                        "linhas", List.of(
                            buildLine("CPTM-L11", 11, "Coral", "#EB601F",
                                "OperacaoNormal", "Operação Normal", null),
                            buildLine("CPTM-L12", 12, "Safira", "#1B2477",
                                "VelocidadeReduzida", "Velocidade Reduzida",
                                "Estamos circulando com velocidade reduzida")
                        )
                    ),
                    Map.of(
                        "uid", "TICTRENS",
                        "nome", "TIC Trens",
                        "linhas", List.of(
                            buildLine("TICTRENS-L7", 7, "Rubi", "#AC184A",
                                "OperacaoNormal", "Operação Normal", null)
                        )
                    )
                )
            )
        );
    }

    private Map<String, Object> buildLine(String uid, int numero, String nome,
            String corRgb, String statusCode, String statusLabel,
            String descricao) {
        return Map.of(
            "uid", uid,
            "numero", numero,
            "nome", nome,
            "corRgb", corRgb,
            "statusLinha", buildStatusMap(statusCode, statusLabel, descricao)
        );
    }

    private Map<String, Object> buildStatusMap(String code, String status,
            String descricao) {
        java.util.HashMap<String, Object> map = new java.util.HashMap<>();
        map.put("codigo", code);
        map.put("status", status);
        map.put("descricao", descricao);
        return map;
    }
}
