package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.StatisticsDto;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.repository.PersonRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    private final CarService carService;
    private final PersonService personService;

    public StatisticService(CarService carService, PersonService personService) {
        this.carService = carService;
        this.personService = personService;
    }

    @Cacheable(value = "statistics")
    public StatisticsDto getStatistics() {
        return new StatisticsDto(
                personService.count(),
                carService.count(),
                carService.countByVendor(),
                personService.countPeopleWithoutCars(),
                carService.countAvgHorsepower()
        );
    }
}
