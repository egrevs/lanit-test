package com.egrevs.project.lanittest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record PersonWithCarsResponse(
        @NotNull
        Long id,
        @NotNull
        String name,
        @NotNull
        @JsonFormat(pattern = "dd.MM.yyyy")
        Date birthday,
        @NotNull
        List<CarResponse> carList
) {
}
