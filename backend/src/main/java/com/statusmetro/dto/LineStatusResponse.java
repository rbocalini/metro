package com.statusmetro.dto;

import java.util.List;

public record LineStatusResponse(
    String lastUpdated,
    List<LineGroupDTO> groups
) {}
