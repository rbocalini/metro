package com.statusmetro.controller;

import com.statusmetro.dto.LineStatusResponse;
import com.statusmetro.service.LineStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Line Status", description = "São Paulo rail line status")
public class LineStatusController {

    private final LineStatusService lineStatusService;

    public LineStatusController(LineStatusService lineStatusService) {
        this.lineStatusService = lineStatusService;
    }

    @GetMapping("/line-status")
    @Operation(summary = "Get current line status",
            description = "Returns all São Paulo rail lines grouped into Metrô and Trens with current operational status")
    public LineStatusResponse getLineStatus() {
        return lineStatusService.getLineStatus();
    }
}
