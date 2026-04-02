package com.statusmetro.dto;

import java.util.List;

public record LineGroupDTO(
    String name,
    List<LineDTO> lines
) {}
