package com.egrevs.project.lanittest.service.validator;

import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.InvalidFieldException;
import com.egrevs.project.lanittest.exception.PersonWithoutCarsException;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonValidatorTest {

    private final PersonValidator personValidator = new PersonValidator();

    private final Person person = DataUtils.getDmitriyPerson();

    @Test
    @DisplayName("Test validator throwing exception functionality")
    public void givenValidatorPersonRequest_whenValidateBirthdate_thenThrowException() {
        //given
        CreatePersonRequest personRequest = new CreatePersonRequest(
                person.getId(),
                person.getName(),
                LocalDate.of(3000, 1, 1)
        );
        //when
        //then
        assertThrows(InvalidFieldException.class, () -> personValidator.validateBirthdate(personRequest));
    }

    @Test
    @DisplayName("Test birthdate validator passing exception functionality")
    public void givenValidatorPersonRequest_whenValidateBirthdate_thenNoExceptionThrown() {
        //given
        CreatePersonRequest personRequest = new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday()
        );
        //when
        //then
        assertDoesNotThrow(() -> personValidator.validateBirthdate(personRequest));
    }

    @Test
    @DisplayName("Test owning cars validator passing exception functionality")
    public void givenValidatorPersonRequest_whenValidateCarsOwning_thenNoExceptionThrown() {
        //given
        Person person = DataUtils.getAdultAndreyPerson();
        person.setCars(List.of(DataUtils.getAndreyBmwCar(), DataUtils.getAndreyToyotaCar()));
        //when

        //then
        assertThat(person.getCars()).isNotEmpty();
        assertDoesNotThrow(() -> personValidator.validatePersonWithCars(person));
    }

    @Test
    @DisplayName("Test owning cars validator throwing exception functionality")
    public void givenValidatorPersonRequest_whenValidateCarsOwning_thenExceptionIsThrown() {
        //given
        Person person = DataUtils.getAdultAndreyPerson();
        //when

        //then
        assertThat(person.getCars()).isEmpty();
        assertThrows(PersonWithoutCarsException.class, () -> personValidator.validatePersonWithCars(person));
    }
}