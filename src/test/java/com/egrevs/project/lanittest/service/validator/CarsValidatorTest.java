package com.egrevs.project.lanittest.service.validator;

import com.egrevs.project.lanittest.dto.CreateCarRequest;
import com.egrevs.project.lanittest.entity.Car;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.InvalidFieldException;
import com.egrevs.project.lanittest.exception.PersonIsNotAdultException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.repository.PersonRepository;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CarsValidatorTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CarsValidator carsValidator;

    @Test
    @DisplayName("Test right validate car method functionality")
    public void givenCarCreationRequest_whenValidateByOwner_thenPassWithoutExceptions() {
        //given
        Person person = DataUtils.getAdultAndreyPerson();
        Car carToCheck = DataUtils.getToyotaMarkCar();
        CreateCarRequest carRequest = new CreateCarRequest(
                carToCheck.getId(),
                carToCheck.getModel() + "-" + carToCheck.getVendor(),
                carToCheck.getHorsepower(),
                person.getId()
        );
        BDDMockito.given(personRepository.findById(carRequest.ownerId()))
                .willReturn(Optional.of(person));
        //when
        //then
        assertDoesNotThrow(() -> carsValidator.validateCarRequest(carRequest));
        verify(personRepository).findById(carRequest.ownerId());
    }

    @Test
    @DisplayName("Test invalid ownerId functionality")
    public void givenCarCreationRequest_whenValidateByOwner_thenThrowsExceptions() {
        //given
        Person person = DataUtils.getDmitriyPerson();
        Car carToCheck = DataUtils.getToyotaMarkCar();
        CreateCarRequest carRequest = new CreateCarRequest(
                carToCheck.getId(),
                carToCheck.getVendor() + "-" + carToCheck.getModel(),
                carToCheck.getHorsepower(),
                2L
        );
        BDDMockito.given(personRepository.findById(carRequest.ownerId()))
                .willReturn(Optional.empty());
        //when
        //then
        assertThat(person.getId()).isNotEqualTo(carRequest.ownerId());
        assertThrows(PersonNotFoundException.class, () -> carsValidator.validateCarRequest(carRequest));
        verify(personRepository).findById(carRequest.ownerId());
    }

    @Test
    @DisplayName("Test model validation functionality")
    public void givenCarCreationRequest_whenValidateByModelFormat_thenThrowsException() {
        //given
        Person person = DataUtils.getAdultAndreyPerson();
        Car carToCheck = DataUtils.getToyotaMarkCar();
        CreateCarRequest carRequest = new CreateCarRequest(
                carToCheck.getId(),
                carToCheck.getModel(),
                carToCheck.getHorsepower(),
                person.getId()
        );
        BDDMockito.given(personRepository.findById(carRequest.ownerId()))
                .willReturn(Optional.of(person));
        //when
        //then
        assertThrows(InvalidFieldException.class, () -> carsValidator.validateCarRequest(carRequest));
        verify(personRepository).findById(carRequest.ownerId());
    }

    @Test
    @DisplayName("Test birthdate validation functionality")
    public void givenCarCreationRequest_whenValidateByBirthday_thenThrowsException() {
        //given
        Person person = DataUtils.getSergeyPerson();
        Car carToCheck = DataUtils.getToyotaMarkCar();
        CreateCarRequest carRequest = new CreateCarRequest(
                carToCheck.getId(),
                carToCheck.getVendor() + "-" + carToCheck.getModel(),
                carToCheck.getHorsepower(),
                person.getId()
        );
        BDDMockito.given(personRepository.findById(carRequest.ownerId()))
                .willReturn(Optional.of(person));
        //when
        //then
        assertThrows(PersonIsNotAdultException.class, () -> carsValidator.validateCarRequest(carRequest));
        verify(personRepository).findById(carRequest.ownerId());
    }
}