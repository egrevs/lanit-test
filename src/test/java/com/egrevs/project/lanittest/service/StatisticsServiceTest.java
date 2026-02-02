package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.StatisticsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private PersonService personService;

    @Mock
    private CarService carService;

    @InjectMocks
    private StatisticService statisticService;

    @Test
    @DisplayName("Test statistics get functionality")
    void givenRepositoriesCounts_whenGetStatistics_thenStatisticIsReturned() {
        //given
        BDDMockito.given(personService.count()).willReturn(3L);
        BDDMockito.given(carService.count()).willReturn(7L);
        BDDMockito.given(carService.countByVendor()).willReturn(2L);
        BDDMockito.given(carService.countAvgHorsepower()).willReturn(230L);
        BDDMockito.given(personService.countPeopleWithoutCars()).willReturn(1L);
        //when
        StatisticsDto statisticsDto = statisticService.getStatistics();
        //then
        assertNotNull(statisticsDto);
        assertThat(statisticsDto.personCount()).isEqualTo(3L);
        assertThat(statisticsDto.carCount()).isEqualTo(7L);
        assertThat(statisticsDto.uniqueVendorCount()).isEqualTo(2L);
        assertThat(statisticsDto.avgHorsepower()).isEqualTo(230L);
        assertThat(statisticsDto.peopleWithoutCars()).isEqualTo(1L);

        verify(personService).count();
        verify(carService).count();
        verify(carService).countByVendor();
        verify(carService).countAvgHorsepower();
        verify(personService).countPeopleWithoutCars();
    }
}