package com.example.demo.web.dto;

import jakarta.validation.constraints.*;

public record BookRequest(
        @NotBlank @Size(max=120) String title,
        @NotBlank @Size(max=80)  String author,
        @PositiveOrZero Integer price
) {}
