package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CarResponse;
import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.PersonWithCarsResponse;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.PersonAlreadyExistsException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        personRepository.save(person);
    }

    public PersonWithCarsResponse getPeopleWithCars(Long personId) {
        Person person = personRepository.findById(personId).orElseThrow(
                () -> new PersonNotFoundException("Пользователь с таким id не существует")
        );

        return toDto(person);
    }

    public void clearAll() {
        personRepository.deleteAll();
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
                car.getVendor() + "-" + car.getModel(),
                car.getHorsepower()
        );
    }
}
