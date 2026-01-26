package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CarResponse;
import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.PersonWithCarsResponse;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.PersonAlreadyExistsException;
import com.egrevs.project.lanittest.exception.PersonIsNotAdultException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void createPerson(CreatePersonRequest personRequest) {
        if (personRepository.existsById(personRequest.id()))
            throw new PersonAlreadyExistsException("Человек с таким id уже существует");

        Person person = new Person();
        person.setId(personRequest.id());
        person.setName(personRequest.name());
        person.setBirthday(personRequest.birthday());

        if (isAdult(person)) {
            personRepository.save(person);
        } else
            throw new PersonIsNotAdultException("Пользователю меньше 18 лет");
    }

    public PersonWithCarsResponse getPeopleWithCars(Long personId) {
        Person person = personRepository.findById(personId).orElseThrow(
                () -> new PersonNotFoundException("Пользователь с таким id не существует")
        );

        return toDto(person);
    }

    private boolean isAdult(Person person) {
        return Period.between(
                        person.getBirthday()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),
                        LocalDate.now())
                .getYears() > 18;
    }

    private PersonWithCarsResponse toDto(Person person) {
        return new PersonWithCarsResponse(
                person.getId(),
                person.getName(),
                person.getBirthday(),
                person.getCars().stream().map(this::toDto).toList()
        );
    }

    private CarResponse toDto(Car car) {
        return new CarResponse(
                car.getId(),
                car.getModel(),
                car.getHorsepower()
        );
    }
}
