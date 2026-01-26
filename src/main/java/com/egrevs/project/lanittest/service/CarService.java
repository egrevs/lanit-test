package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.CarAlreadyExistsException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private CarRepository carRepository;
    private PersonRepository personRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void createCar(CreateCarRequest carRequest) {
        if (carRepository.existsById(carRequest.id()))
            new CarAlreadyExistsException("Машина с таким ID уже существует");

        Person person = personRepository.findById(carRequest.ownerId()).orElseThrow(
                () -> new PersonNotFoundException("Пользователя с таким ownerID не существует")
        );

        Car car = new Car();
        car.setId(carRequest.id());
        car.setModel(carRequest.model());
        car.setHorsepower(carRequest.horsepower());
        car.setOwner(person);

        carRepository.save(car);
    }
}
