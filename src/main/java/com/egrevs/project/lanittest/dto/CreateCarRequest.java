package com.egrevs.project.lanittest.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCarRequest(
        @NotNull
        Long id,
        @NotNull
        String model,
        @NotNull
        Integer horsepower,
        @NotNull
        Long ownerId
) {
}
