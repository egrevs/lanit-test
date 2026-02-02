package com.egrevs.project.lanittest.dto;

public record StatisticsDto(
        Long personCount,
        Long carCount,
        Long uniqueVendorCount,
        Long peopleWithoutCars,
        Long avgHorsepower
) {
}
