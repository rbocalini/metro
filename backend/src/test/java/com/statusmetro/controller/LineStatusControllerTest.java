package com.statusmetro.controller;

import com.statusmetro.config.ExternalApiException;
import com.statusmetro.dto.*;
import com.statusmetro.service.LineStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class LineStatusControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private LineStatusService lineStatusService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldReturnLineStatusAsJson() throws Exception {
        when(lineStatusService.getLineStatus()).thenReturn(buildSampleResponse());

        mockMvc.perform(get("/api/line-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lastUpdated").value("2026-04-02T17:00:04"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups.length()").value(2))
                .andExpect(jsonPath("$.groups[0].name").value("Metrô"))
                .andExpect(jsonPath("$.groups[1].name").value("Trens"));
    }

    @Test
    void shouldReturnLineDetailsInGroup() throws Exception {
        when(lineStatusService.getLineStatus()).thenReturn(buildSampleResponse());

        mockMvc.perform(get("/api/line-status"))
                .andExpect(jsonPath("$.groups[0].lines[0].uid").value("METRO-L1"))
                .andExpect(jsonPath("$.groups[0].lines[0].number").value(1))
                .andExpect(jsonPath("$.groups[0].lines[0].name").value("Azul"))
                .andExpect(jsonPath("$.groups[0].lines[0].operator").value("Metro SP"))
                .andExpect(jsonPath("$.groups[0].lines[0].colorHex").value("#171796"))
                .andExpect(jsonPath("$.groups[0].lines[0].status.code").value("OperacaoNormal"))
                .andExpect(jsonPath("$.groups[0].lines[0].status.label").value("Operação Normal"))
                .andExpect(jsonPath("$.groups[0].lines[0].category").value("metro"));
    }

    @Test
    void shouldReturn502WhenExternalApiUnavailable() throws Exception {
        when(lineStatusService.getLineStatus())
                .thenThrow(new ExternalApiException("Unable to fetch line status from data source. Please try again later."));

        mockMvc.perform(get("/api/line-status"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").value("EXTERNAL_API_UNAVAILABLE"))
                .andExpect(jsonPath("$.message").exists());
    }

    private LineStatusResponse buildSampleResponse() {
        return new LineStatusResponse(
            "2026-04-02T17:00:04",
            List.of(
                new LineGroupDTO("Metrô", List.of(
                    new LineDTO("METRO-L1", 1, "Azul", "Metro SP", "#171796",
                        new LineStatusDTO("OperacaoNormal", "Operação Normal", null),
                        "metro")
                )),
                new LineGroupDTO("Trens", List.of(
                    new LineDTO("CPTM-L11", 11, "Coral", "CPTM", "#EB601F",
                        new LineStatusDTO("OperacaoNormal", "Operação Normal", null),
                        "trens")
                ))
            )
        );
    }
}
