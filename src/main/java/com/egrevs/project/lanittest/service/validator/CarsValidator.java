package com.egrevs.project.lanittest.service.validator;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.CarAlreadyExistsException;
import com.egrevs.project.lanittest.exception.InvalidFieldException;
import com.egrevs.project.lanittest.exception.PersonIsNotAdultException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.repository.CarRepository;
import com.egrevs.project.lanittest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Component
public class CarsValidator {

    private CarRepository carRepository;
    private PersonRepository personRepository;

    @Autowired
    public CarsValidator(CarRepository carRepository, PersonRepository personRepository) {
        this.carRepository = carRepository;
        this.personRepository = personRepository;
    }

    public void validateCarRequest(CreateCarRequest request){
        validateId(request.id());
        validateOwner(request.ownerId());
        validateModel(request.model());
    }

    private void validateId(Long id){
        if (carRepository.existsById(id))
            throw new CarAlreadyExistsException("Машина с таким ID уже существует");
    }

    private void validateOwner(Long ownerId){
        Person person = personRepository.findById(ownerId).orElseThrow(
                () -> new PersonNotFoundException("Пользователя с таким ownerID не существует")
        );
        if (!isAdult(person))
            throw new PersonIsNotAdultException("Пользователь должен быть старше 18 лет");
    }

    private void validateModel(String model){
        String[] vendorModel = model.split("-", 2);

        if (vendorModel.length < 2){
            throw new InvalidFieldException("Должны быть указаны и vendor, и model");
        }

        if (vendorModel[0].trim().isEmpty() || vendorModel[1].trim().isEmpty()) {
            throw new InvalidFieldException("Vendor и model не должны быть пустыми");
        }
    }

    private boolean isAdult(Person person) {
        return Period.between(
                        person.getBirthday()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),
                        LocalDate.now())
                .getYears() >= 18;
    }
}
