package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.InvalidFieldException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.repository.PersonRepository;
import com.egrevs.project.lanittest.service.validator.CarsValidator;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final PersonRepository personRepository;
    private final CarsValidator carsValidator;

    public CarService(CarRepository carRepository, PersonRepository personRepository, CarsValidator carsValidator) {
        this.carRepository = carRepository;
        this.personRepository = personRepository;
        this.carsValidator = carsValidator;
    }

    public void createCar(CreateCarRequest carRequest) {
        Person person = personRepository.findById(carRequest.ownerId()).orElseThrow(
                () -> new PersonNotFoundException("Пользователя с таким ownerID не существует")
        );

        carsValidator.validateCarRequest(carRequest);

        if (!carRequest.model().contains("-")) {
            throw new InvalidFieldException("Неверный формат записи model");
        }

        String[] array = carRequest.model().split("-", 2);

        Car car = new Car();
        car.setId(carRequest.id());
        car.setVendor(array[0].trim());
        car.setModel(array[1].trim());
        car.setHorsepower(carRequest.horsepower());
        car.setOwner(person);

        carRepository.save(car);
    }

    public long count() {
        return carRepository.count();
    }

    public long countByVendor() {
        return carRepository.countByVendor();
    }
}
