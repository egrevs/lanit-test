package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.StatisticsDto;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    private final PersonRepository personRepository;
    private final CarRepository carRepository;

    public StatisticService(PersonRepository personRepository, CarRepository carRepository) {
        this.personRepository = personRepository;
        this.carRepository = carRepository;
    }

    public StatisticsDto getStatistics(){
        return new StatisticsDto(
                personRepository.count(),
                carRepository.count(),
                carRepository.countByVendor()
        );
    }
}
