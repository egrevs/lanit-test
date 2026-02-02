package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.StatisticsDto;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class StatisticsServiceCacheTest {

    @SpyBean
    private PersonService personService;

    @SpyBean
    private CarService carService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void cleanUp(){
        personService.clearAll();
    }

    @Test
    @DisplayName("Test cache working functionality")
    void givenStatisticsCache_whenGetStatistics_thenCacheIsWorked(){
        //given
        Cache cache = cacheManager.getCache("statistics");
        assertThat(cache).isNotNull();
        cache.clear();
        //when
        StatisticsDto statistics1 = statisticService.getStatistics();
        StatisticsDto statistics2 = statisticService.getStatistics();
        //then
        verify(personService, times(1)).count();
        verify(personService, times(1)).countPeopleWithoutCars();
        verify(carService, times(1)).count();
        verify(carService, times(1)).countAvgHorsepower();
        verify(carService, times(1)).countByVendor();

        assertThat(statistics2).isEqualTo(statistics1);
        assertThat(cache.get(SimpleKey.EMPTY, StatisticsDto.class)).isEqualTo(statistics1);
    }

    @Test
    @DisplayName("Test cache evict on person functionality")
    void givenStatisticsCache_whenCreateNewPerson_thenCacheIsWorked(){
        //given
        Cache cache = cacheManager.getCache("statistics");
        assertThat(cache).isNotNull();
        cache.clear();
        //when
        StatisticsDto statistics1 = statisticService.getStatistics();
        Person person = DataUtils.getAdultAndreyPerson();
        personService.createPerson(new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday())
        );
        StatisticsDto statistics2 = statisticService.getStatistics();
        //then
        verify(personService, times(2)).count();
        verify(personService, times(2)).countPeopleWithoutCars();
        verify(carService, times(2)).count();
        verify(carService, times(2)).countByVendor();
        verify(carService, times(2)).countAvgHorsepower();

        assertThat(statistics1).isNotEqualTo(statistics2);
        assertThat(cache.get(SimpleKey.EMPTY, StatisticsDto.class)).isEqualTo(statistics2);
    }

    @Test
    @DisplayName("Test cache evict on car functionality")
    void givenStatisticsCache_whenCreateNewCar_thenCacheIsWorked(){
        //given
        Cache cache = cacheManager.getCache("statistics");
        assertThat(cache).isNotNull();
        cache.clear();
        //when
        StatisticsDto statistics1 = statisticService.getStatistics();
        Person person = DataUtils.getAdultAndreyPerson();
        personService.createPerson(new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday())
        );
        Car car = DataUtils.getAndreyBmwCar();
        carService.createCar(new CreateCarRequest(
                car.getId(),
                car.getVendor() + "-" + car.getModel(),
                car.getHorsepower(),
                person.getId()));
        StatisticsDto statistics2 = statisticService.getStatistics();
        //then
        verify(personService, times(2)).count();
        verify(personService, times(2)).countPeopleWithoutCars();
        verify(carService, times(2)).count();
        verify(carService, times(2)).countByVendor();
        verify(carService, times(2)).countAvgHorsepower();

        assertThat(statistics1).isNotEqualTo(statistics2);
        assertThat(cache.get(SimpleKey.EMPTY, StatisticsDto.class)).isEqualTo(statistics2);
    }
}