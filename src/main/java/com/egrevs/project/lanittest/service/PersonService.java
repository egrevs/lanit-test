package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CarResponse;
import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.PersonWithCarsResponse;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.PersonAlreadyExistsException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.repository.PersonRepository;
import com.egrevs.project.lanittest.service.validator.PersonValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private PersonRepository personRepository;

    private PersonValidator personValidator;

    public PersonService(PersonRepository personRepository, PersonValidator personValidator) {
        this.personRepository = personRepository;
        this.personValidator = personValidator;
    }

    @CacheEvict(value = "statistics", allEntries = true)
    public void createPerson(CreatePersonRequest personRequest) {
        if (personRepository.existsById(personRequest.id()))
            throw new PersonAlreadyExistsException("Человек с таким id уже существует");

        personValidator.validateBirthdate(personRequest);

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
        personValidator.validateBirthdate(toRequestDto(person));

        return toDto(person);
    }

    @CacheEvict(value = "statistics")
    public void clearAll() {
        personRepository.deleteAll();
    }

    public long count() {
        return personRepository.count();
    }

    public long countPeopleWithoutCars(){
        return personRepository.countPeopleWithoutCars();
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

    private CreatePersonRequest toRequestDto(Person person) {
        return new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday()
        );
    }
}
