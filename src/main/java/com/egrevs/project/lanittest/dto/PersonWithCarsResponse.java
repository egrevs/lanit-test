package com.egrevs.project.lanittest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record PersonWithCarsResponse(
        @NotNull
        Long id,
        @NotNull
        String name,
        @NotNull
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate birthday,
        @NotNull
        List<CarResponse> cars
) {
}
