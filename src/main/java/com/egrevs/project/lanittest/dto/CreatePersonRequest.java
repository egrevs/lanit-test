package com.egrevs.project.lanittest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public record CreatePersonRequest(
        @NotNull
        Long id,
        @NotNull
        String name,
        @NotNull
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate birthday
) {
}
