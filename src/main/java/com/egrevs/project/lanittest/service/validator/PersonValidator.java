package com.egrevs.project.lanittest.service.validator;

import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.InvalidFieldException;
import com.egrevs.project.lanittest.exception.PersonWithoutCarsException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class PersonValidator {

    public void validateBirthdate(CreatePersonRequest personRequest) {
        LocalDate localDate = personRequest.birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (localDate.isAfter(LocalDate.now()))
            throw new InvalidFieldException("Невалидная дата рождения");
    }

    public void validatePersonWithCars(Person person) {
        if (person.getCars().isEmpty())
            throw new PersonWithoutCarsException("У пользователя нет машин во владении");
    }
}
