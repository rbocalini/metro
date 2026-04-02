package com.statusmetro.dto;

public record LineDTO(
    String uid,
    int number,
    String name,
    String operator,
    String colorHex,
    LineStatusDTO status,
    String category
) {}
