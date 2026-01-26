package com.egrevs.project.lanittest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCarRequest(
        @NotNull
        Long id,
        @NotNull
        String model,
        @NotNull
        @Min(1)
        Integer horsepower,
        @NotNull
        Long ownerId
) {
}
