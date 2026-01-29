package com.egrevs.project.lanittest.service;

import com.egrevs.project.lanittest.dto.CreatePersonRequest;
import com.egrevs.project.lanittest.dto.PersonWithCarsResponse;
import com.egrevs.project.lanittest.entity.Person;
import com.egrevs.project.lanittest.exception.InvalidFieldException;
import com.egrevs.project.lanittest.exception.PersonAlreadyExistsException;
import com.egrevs.project.lanittest.exception.PersonNotFoundException;
import com.egrevs.project.lanittest.exception.PersonWithoutCarsException;
import com.egrevs.project.lanittest.repository.PersonRepository;
import com.egrevs.project.lanittest.service.validator.PersonValidator;
import com.egrevs.project.lanittest.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonValidator personValidator;

    @InjectMocks
    private PersonService personService;

    @Test
    @DisplayName("Test save person functionality")
    public void givenPersonToSaveRequest_whenSavePerson_thenRepositoryIsCalled() {
        //given
        Person person = DataUtils.getDmitriyPerson();
        CreatePersonRequest personToSave = new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday()
        );
        BDDMockito.given(personRepository.existsById(personToSave.id()))
                .willReturn(false);
        BDDMockito.given(personRepository.save(any(Person.class)))
                .willReturn(person);
        //when
        personService.createPerson(personToSave);
        //then
        BDDMockito.verify(personValidator).validateBirthdate(personToSave);
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);

        BDDMockito.then(personRepository).should(times(1)).save(personCaptor.capture());
        Person capturedPerson = personCaptor.getValue();

        assertThat(capturedPerson.getId()).isEqualTo(personToSave.id());
        assertThat(capturedPerson.getName()).isEqualTo(personToSave.name());
        assertThat(capturedPerson.getBirthday()).isEqualTo(personToSave.birthday());
    }

    @Test
    @DisplayName("Test save present person functionality")
    public void givenPersonToSaveRequest_whenSavePerson_thenExceptionIsThrown() {
        //given
        Person person = DataUtils.getVladimirPerson();
        CreatePersonRequest personToSave = new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday()
        );
        BDDMockito.given(personRepository.existsById(person.getId()))
                .willReturn(true);
        //when
        assertThrows(PersonAlreadyExistsException.class, () -> personService.createPerson(personToSave));
        //then
        verify(personRepository, never()).save(any(Person.class));
    }


    @Test
    @DisplayName("Test getting person with cars functionality")
    public void givenPersonWithCars_whenGetPersonWithCars_thenRepositoryIsCalled() {
        //given
        Person person = DataUtils.getAdultAndreyPerson();
        person.setCars(List.of(DataUtils.getAndreyBmwCar(), DataUtils.getAndreyToyotaCar()));
        BDDMockito.given(personRepository.findById(person.getId()))
                .willReturn(Optional.of(person));
        //when
        PersonWithCarsResponse personWithCarsResponse = personService.getPeopleWithCars(person.getId());
        //then
        BDDMockito.verify(personRepository).findById(person.getId());
        BDDMockito.verify(personValidator).validateBirthdate(new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday()
        ));
        BDDMockito.verify(personValidator).validatePersonWithCars(person);

        assertThat(personWithCarsResponse).isNotNull();
        assertThat(personWithCarsResponse.cars()).hasSize(2);
        assertDoesNotThrow(() -> personValidator.validatePersonWithCars(person));
    }

    @Test
    @DisplayName("Test getting unknown person functionality")
    public void givenPersonWithCars_wheGetPersonWithCars_thenExceptionIsThrown() {
        //given
        Long nonExistentPersonId = 10000L;
        BDDMockito.given(personRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        assertThrows(PersonNotFoundException.class, () -> personService.getPeopleWithCars(nonExistentPersonId));
        //then
        verify(personRepository).findById(nonExistentPersonId);
    }

    @Test
    @DisplayName("Test getting person with cars throwing exception functionality")
    public void givenPersonWithoutCars_whenGetPersonWithCars_thenExceptionIsThrown() {
        //given
        Person person = DataUtils.getAdultAndreyPerson();
        BDDMockito.given(personRepository.findById(person.getId()))
                .willReturn(Optional.of(person));
        //when
        BDDMockito.willThrow(new PersonWithoutCarsException("У пользователя нет машин"))
                .given(personValidator).validatePersonWithCars(person);
        //then
        assertThrows(PersonWithoutCarsException.class, () -> personService.getPeopleWithCars(person.getId()));
        assertThat(person).isNotNull();
        assertThat(person.getCars()).isEmpty();
    }

    @Test
    @DisplayName("Test validator in save method functionality")
    public void givenPersonToSaveRequest_whenSavePerson_thenValidatorExceptionIsReturned() {
        //given
        Person person = DataUtils.getSergeyPerson();
        CreatePersonRequest personRequest = new CreatePersonRequest(
                person.getId(),
                person.getName(),
                person.getBirthday()
        );
        BDDMockito.given(personRepository.existsById(personRequest.id())).willReturn(false);
        //when
        BDDMockito.willThrow(new InvalidFieldException("Невалидная дата рождения"))
                .given(personValidator).validateBirthdate(personRequest);
        //then
        assertThrows(InvalidFieldException.class, () -> personService.createPerson(personRequest));
        verify(personValidator).validateBirthdate(personRequest);
        verify(personRepository, never()).save(any(Person.class));
    }
}