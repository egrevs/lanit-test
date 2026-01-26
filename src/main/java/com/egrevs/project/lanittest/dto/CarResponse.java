package com.egrevs.project.lanittest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CarResponse(
        @NotNull
        Long id,
        @NotNull
        String model,
        @NotNull
        @Min(1)
        Integer horsepower
) {
}
